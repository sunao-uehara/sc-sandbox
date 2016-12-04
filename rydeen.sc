/*******************************************************************************
"rydeen" -- ymo
*******************************************************************************/
(

Tempo.bpm = 142;
s = Server.default;
s.waitForBoot {// executed after server started
/* ------------------------------------------------------------------------ SynthDefs */

	SynthDef(\hat, {| amp=0.1, pan=0 |
		var sig, env;
		env = EnvGen.kr(Env.perc(0, 0.03), 1, amp, doneAction: 2);
		sig = WhiteNoise.ar;
		sig = HPF.ar(sig, 8000);
		sig = Pan2.ar(sig, pan, env);
		Out.ar(0, sig);
	}).add;

	SynthDef(\snr, {| amp=0.1 |
		var sig, env, snr, mem;
		env = EnvGen.kr(Env.perc(0, 0.05), 1, amp, doneAction: 2);
		snr = WhiteNoise.ar;
		mem = FSinOsc.ar(200);
		sig = LPF.ar(snr + mem, 12000);
		sig = Pan2.ar(sig, 0, env);
		Out.ar(0, sig);
	}).add;

	SynthDef(\kik, {| amp=0.3, sustain=1, freq=30 |
		var sig, frqEnv, ampEnv;
		frqEnv = EnvGen.kr(Env.perc, 1, freq*10, freq, 0.023);
		ampEnv = EnvGen.kr(Env.linen(0.01, 0.1, 0.3, 1, [-5,1,-4]), 1, amp, 0, sustain, 2);
		sig = SinOsc.ar(frqEnv, 0, ampEnv);
		sig = Pan2.ar(sig, 0);
		Out.ar(0, sig);
	}).add;

	SynthDef(\prc, {| out=0, amp=0.1 |
		var sig, env;
		env = EnvGen.kr(Env.perc(0, 0.08), 1, amp, doneAction: 2);
		sig = WhiteNoise.ar;
		sig = sig * env;
		Out.ar(out, sig * env);
	}).add;

	SynthDef(\chorus, {| out=0 |
		var sig;
		sig = In.ar(out, 1) * 0.5;
		sig = DelayC.ar(sig, 1, FSinOsc.kr(0.25, [0, pi]).range(0.02, 0.03), 0.5, sig);
		ReplaceOut.ar(out, sig);
	}).add;

	SynthDef(\bss, {| gate=1, amp=0.1, sustain=1, freq=440 |
		var sig, env;
		env = EnvGen.kr(Env.adsr, gate, amp, 0, sustain, 2);
		sig = LFSaw.ar(freq);
		sig = RLPF.ar(sig, 1000);
		sig = Pan2.ar(sig, 0, env);
		Out.ar(0, sig);
	}).add;

	SynthDef(\arp, {| gate=1, amp=1, sustain=1, freq=440, pan=0 |
		var sig, env;
		env = EnvGen.kr(Env.adsr, gate, amp, 0, sustain, 2);
		sig = LFSaw.ar(freq);
		sig = HPF.ar(sig, 200);
		sig = RLPF.ar(sig, 5000, 0.2);
		sig = Pan2.ar(sig, pan, env);
		Out.ar(0, sig);
	}).add;

	SynthDef(\hrm, {| amp, freq=440 |
		var sig, env;
		env = EnvGen.kr(Env.perc, 1, amp, doneAction: 2);
		sig = LFSaw.ar([freq, freq*0.99]);
		sig = RLPF.ar(sig, 3200);
		Out.ar(0, sig * env);
	}).add;

	SynthDef(\lead, {| out=0, gate=1, amp=1, freq=440 |
		var sig, env;
		env = EnvGen.kr(Env.adsr(0.04, 0.1, 0.5, 0.5), gate, amp, 0, 1, 2);
		sig = Mix.ar(LFTri.ar([freq, freq*2]));
		sig = HPF.ar(sig, 500);
		sig = sig * env;
		Out.ar(out, sig);
	}).add;

	SynthDef(\reverb, {| gate=1, out=0 |
		var sig, rev, env;
		env = Linen.kr(gate, 0.05, 1, 1, 2);
		sig = In.ar(out, 1) * 0.5;
		rev = BPF.ar(sig, 2000);
		4.do { rev = AllpassN.ar(rev, 0.050, [0.050.rand, 0.050.rand], 1) };
		sig = sig + rev;
		sig = sig * env;
		ReplaceOut.ar(out, sig);
	}).add;

/* ------------------------------------------------------------------------ Patterns intro */
~prcIntro = Pfxb(
	Pbind(\instrument, \prc,
		\dur, Pser([0.5, 0.25, 0.25], 3 * 4 * 3),
		\amp, 0.7
	), \chorus
);
~snrIntro = Pbind(\instrument, \snr,
	\dur, Pn(1, 12),
	\degree, Pseq([Pn(\, 10), 1, 1]),
	\amp, 0.7
);
~bssIntro = Pbind(\instrument, \bss,
	\dur, Pn(1, 12),
	\legato, 0.7,
	\amp, 0.6,
	\scale, [2, 4, 5, 7, 9, 10, 12],
	\degree, Pseq([Pn(\, 10), 6, 5]) - 1,
	\octave, 2
);
~hrmIntro = Pbind(\instrument, \hrm,
		\dur, 1,
		\legato, 1,
		\scale, [2, 4, 5, 7, 9, 10, 12],
		\degree, Pseq([Pn(\, 10), [0,-2,2], [1,-0.9,3]], 1),
		\octave, 5,
		\amp, 0.2
);
~leadIntro = Pfxb(
	Pbind(\instrument, \lead,
		\dur, Pn(1, 12),
		\legato, 0.9,
		\amp, 0.3,
		\scale, [2, 4, 5, 7, 9, 10, 12],
		\degree, Pseq([Pn(\, 10), 0, 1], 1),
		\octave, 6
	), \reverb
);
/* --------------------------------------------------------------------- Patterns body A */
~hatA = Pbind(\instrument, \hat,
	\dur, Pn(0.25, 4 * 4 * 8),
	\amp, Pseq([0.4, 0.2, 0.3, 0.2], inf),
	\pan, -0.5
);
~snrA = Pbind(\instrument, \snr,
	\dur, Pseq([
		Pseq([1, 1, 1, 1], 7),
		Pseq([1, 1, 0.5, Pn(0.25, 4), 0.5], 1)
	], 1),
	\amp, 0.6,
	\degree, Pseq([
		Pseq([\, 1, \, 1], 7),
		Pseq([\, 1, \, Pn(1, 4), 1], 1)
	], 1)
);
~kikA = Pbind(\instrument, \kik,
	\dur, Pn(1, 4 * 8),
	\amp, 0.65,
	\freq, 50
);
~prcA = Pfxb(
	Pbind(\instrument, \prc,
		\dur, Pseq([0.5, 0.25, 0.25], 4 * 8),
		\amp, Pseq([0.4, 0.2, 0.2], inf)
	), \chorus
);
~bssA = Pbind(\instrument, \bss,
	\dur, Pseq([
		Pseq([1.5, 1.5, 1.5, 1.5, 1, 1, 2, 0.5, 1, 2.5, 1, 1], 1),
		Pseq([1.5, 1.5, 1.5, 1.5, 1, 1, 2, 2, 2, 1, 1], 1),
	], 1),
	\legato, 0.7,
	\amp, 0.6,
	\scale, [2, 4, 5, 7, 9, 10, 12],
	\degree, Pseq([
		Pseq([1, 2, 3, 1, 7, 3, 6, \, 5, 4, 6, 5], 1) - 1,
		Pseq([1, 2, 3, 1, 7, 3, 6, 6, 6, 6, 5], 1) - 1
	], inf),
	\octave, Pseq([
		Pseq([3, 3, 3, 3, 2, 3, 2, 2, 2, 2, 2, 2], 1),
		Pseq([3, 3, 3, 3, 2, 3, 2, 2, 2, 2, 2], 1)
	], inf)
);
~arpA = Pbind(\instrument, \arp,
	\dur, Pseq([
		Pn(0.25, 4 * 14),
		1, 1
	], 2),
	\legato, 0.5,
	\amp, 0.45,
	\scale, [2, 4, 5, 7, 9, 10, 12],
	\degree, Pseq([
		Pseq([1, 1, 1, 1, 1, 8, 5, 1], 7),
		1, 1
	], inf) - 1,
	\octave, 3,
	\mtranspose, Pseq([
		Pn(0,  4 * 8),
		Pn(-2, 4 * 4),
		Pn(-4, 4 * 2),
		-2, -3
	], inf),
	\pan, Pseq([
		Pser([-1, 1], 4 * 14),
		0, 0
	], inf)
);
~hrmA = Pbind(\instrument, \hrm,
	\dur, Pseq([
		Pseq([1.5, 1.5, 1.5, 1.5, 1, 1, 2, 0.5, 1, 2.5, 1, 1], 1),
		Pseq([1.5, 1.5, 1.5, 1.5, 1, 1, 2, 2, 2, 1, 1], 1)
	], 1),
	\legato, 1,
	\scale, [2, 4, 5, 7, 9, 10, 12],
	\degree, Pseq([
		Pseq([
			[2,0,-3], [3,0,-3], [4,0,-3], [2,0,-3], [1,-1,-3], [4,1,-1],
			[2,0,-3,-2], \, [1,-1,-4,-3], [0,-2,-5,-4], [0,-2,-5], [1,-0.9,-4]
		], 1),
		Pseq([
			[2,0,-3], [3,0,-3], [4,0,-3], [2,0,-3], [1,-1,-3], [4,1,-1],
			[2,0,-2], [4,2,0,-1], [7,5,4,2], [9,7,6,4], [8,6,4,3]
		], 1)
	], inf),
	\amp, 0.2
);
~leadA = Pfxb(
	Pbind(\instrument, \lead,
		\dur, Pseq([
			4, Pn(0.5, 5),
			0.25, 0.25, 0.5, 0.5, 6, 1, 1,
			4, Pn(0.5, 5),
			0.25, 0.25, 0.5, 0.5, 6, 1, 1
		], 1),
		\legato, 1,
		\amp, 0.55,
		\scale, [2, 4, 5, 7, 9, 10, 12],
		\degree, Pseq([
			2, \, 2, 3, 2, 1, 1, 0, -1, -3, 0, 0, 1,
			2, \, 2, 3, 2, 1, 1, 0, -1, 0, 4, \, \
		], inf),
		\octave, 6
	), \reverb
);
/* --------------------------------------------------------------------- Patterns body B */
~hatB = ~hatA;
~snrB = Pbind(\instrument, \snr,
		\dur, Pseq([1, 1, 1, 1], 8),
		\amp, 0.6,
		\degree, Pseq([
			Pseq([\, 1, \, 1], 7),
			Pseq([\, 1, 1, 1], 1)
		], 1)
);
~kikB = Pbind(\instrument, \kik,
		\dur, Pseq([
			Pseq([1, 1, 0.5, 0.5, 1], 7),
			Pseq([1, 1, 2], 1),
		], 1),
		\amp, 0.7,
		\freq, Pseq([
			Pseq([50, \, 50, 50, \], 7),
			Pseq([50, 50, \], 1),
		], 1)
);
~prcB = Pfxb(
	Pbind(\instrument, \prc,
		\dur, Pseq([0.5, 0.25, 0.25], 4 * 8),
		\amp, Pseq([0.7, 0.5, 0.5], inf)
	), \chorus
);
~bssB = Pbind(\instrument, \bss,
		\dur, Pseq([
			0.5, 0.5, 1.5, 0.5, 0.5, 2.5, 0.5, 0.5, 1,
			0.5, 0.5, 1.5, 0.5, 0.5, 2.5, 1, 1,
			1.5, 1.5, 1.5, 1.5, 2,
			Pn(0.5, 7), 2.5, 1, 1
		], 1),
		\legato, 0.7,
		\amp, 0.65,
		\scale, [2, 4, 5, 7, 9, 10, 12],
		\degree, Pseq([
			3, 2, 3, 4, 6, 7, 7, 6, 4,
			3, 2, 3, 4, 6, 7, 7, 6,
			5, 3, 4, 5, 5.1, Pn(-1, 5), 7, 6, 2, 5, 4
		], 1),
		\octave, 2
);
~arpB = Pbind(\instrument, \arp,
		\dur, Pseq([Pn(0.25, 4 * 4 * 7 + 8), 1, 1], 1),
		\legato, 0.5,
		\amp, 0.35,
		\scale, [2, 4, 5, 7, 9, 10, 12],
		\degree, Pseq([
			Pseq([0,7,0,7,0,7,7,0,7,0,7,0,0,7,0,7], 7),
			Pseq([0,7,0,7,0,7,7,7,-2,-3], 7),
		], 1),
		\octave, 4,
		\mtranspose, Pseq([Pn(0, 4 * 4 * 6), Pn(-1, 4 * 6), 0, 0], 1),
		\pan, Pseq([Pser([-1, 1], 4 * 4 * 7 + 8), 0, 0], inf)
);
~hrmB = Pbind(\instrument, \hrm,
		\dur, Pseq([
			1, 2, 0.5, 4.5, 1, 2, 0.5, 2.5, 1, 0.5, 0.25, 0.25,
			1.5, 1.5, 1.5, 1.5, 2, 0.5, 1, 1, 1, 2.5, 1, 1
		], 1),
		\legato, 1,
		\scale, [2, 4, 5, 7, 9, 10, 12],
		\degree, Pseq([
			\, [3, 4, 6, 8], \, [4, 7, 8, 10], \, [3, 4, 6, 8], \, [4, 7, 8, 10], 5, 4, 5, 4,
			3, [4, 6, 8, 10], [5, 7, 9, 11], [6, 8, 10, 12], [3, 5, 6.1, 8],
			\, 6, 8, 10, 12, [6, 8, 10], [5, 7, 9]
		], inf) - 1,
		\octave, Pseq([Pn(5, 13), Pn(4, 3), Pn(3, 1), Pn(4, 5), 4, 4], 1),
		\amp, 0.2
);
~leadB = Pfxb(
	Pbind(\instrument, \lead,
		\dur, Pseq([
			1, 0.5, 0.5, 0.5, 1, 0.5,
			0.5, 0.5, Pn(0.5/3, 3), 0.5, 0.5, 1.5,
			1, 0.5, 0.5, 0.5, 1, 0.5,
			0.5, Pn(0.5/3, 3), 0.5, 0.5, 1, 0.5, 0.5,
			2.5, Pn(0.5/3, 3), 0.5, 0.5,
			2, 1, 1,
			2.5, 1, 0.5,
			2, 1, 1
		], 1),
		\legato, 1,
		\amp, 0.5,
		\scale, [2, 4, 5, 7, 9, 10, 12],
		\degree, Pseq([
			7, 7, 6, 7, 9, 10,
			6, 7, 6, 7, 6, 4, 6, 7,
			7, 7, 6, 7, 9, 10,
			11, 10, 11, 10, 9, 7, 9, 7, 9,
			11, 13, 14, 13, 11, 10,
			11, 7, 9,
			11, 10, 11,
			10, 7, 8
		], inf),
		\octave, 5
	), \reverb
);
~intro = Ppar([~prcIntro, ~snrIntro, ~bssIntro, ~hrmIntro, ~leadIntro], 1);
~bodyA = Ppar([~arpA, ~prcA, ~leadA, ~hrmA, ~bssA, ~hatA, ~snrA, ~kikA], 1);
~bodyB = Ppar([~arpB, ~prcB, ~leadB, ~hrmB, ~bssB, ~hatB, ~snrB, ~kikB], 1);
});
// play !!
~rydeen = Pseq([~intro, Pseq([~bodyA, ~bodyB], inf)]).play;
// stop
~rydeen.stop;
