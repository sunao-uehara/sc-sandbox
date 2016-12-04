s.boot;

// hat
(
SynthDef(\hat, {| amp=0.1, pan |
	var sig, env;
	env = EnvGen.kr(Env.perc(0, 0.03), 1, amp, doneAction: 2);
	sig = WhiteNoise.ar;
	sig = HPF.ar(sig, 8000); // hypass filter, cut lower thant 8000Hz
	sig = sig * env;
	sig = Pan2.ar(sig, pan);//decide panning
	Out.ar(0, sig);//output of sound, start with 0, (left channel for stereo)
}).add;
)

Synth(\hat)
Synth(\hat, [pan:  0]);// middle
Synth(\hat, [pan: -1]);// left
Synth(\hat, [pan:  1]);// right

// snare
(
SynthDef(\snr, {| amp=0.1 |
	var sig, env, snr, mem;
	env = EnvGen.kr(Env.perc(0, 0.05), 1, amp, doneAction: 2);
	snr = WhiteNoise.ar;
	mem = FSinOsc.ar(200);
	sig = LPF.ar(snr + mem, 12000);// lowpass filter, merge with mem and cut over 1.2kHz
	sig = Pan2.ar(sig, 0, env);
	Out.ar(0, sig);
}).add;
)
~snrA = Pbind(\instrument, \snr, \dur, 1).play;
~snrA.stop;

// kick
// create attack of kick by decreasing freq from high to low in 0.023 sec
(
SynthDef(\kik, {| amp=0.3, sustain=1, freq=30 |
	var sig, frqEnv, ampEnv;
	frqEnv = EnvGen.kr(Env.perc, 1, freq*10, freq, 0.023);// freq*10 to freq in 0.023 sec
	ampEnv = EnvGen.kr(Env.linen(0.01, 0.1, 0.3, 1, [-5,1,-4]), 1, amp, 0, sustain, 2);
	sig = SinOsc.ar(frqEnv, 0, ampEnv);
	sig = Pan2.ar(sig, 0);
	Out.ar(0, sig);
}).add;
)
~kikA = Pbind(\instrument, \kik, \dur, 1, \amp, 0.8, \freq, 50).play;
~kikA.stop;


(
// hihat like percussion
// prepare out arg
SynthDef(\prc, {| out=0, amp=0.1 |
	var sig, env;
	env = EnvGen.kr(Env.perc(0, 0.08), 1, amp, doneAction: 2);
	sig = WhiteNoise.ar;
	sig = sig * env;
	Out.ar(out, sig);
}).add;

// chorus effect
SynthDef(\chorus, {| out=0 |
	var sig;
	sig = In.ar(out, 1) * 0.5;// receive mono input
	// apply delay、and automate delay val by FSinOsc
	sig = DelayC.ar(sig, 1, FSinOsc.kr(0.25, [0, pi]).range(0.02, 0.03), 0.5, sig);
	ReplaceOut.ar(out, sig);// override original signal
}).add;
)

sig = DelayC.ar(sig, 1, FSinOsc.kr(0.25, [0, pi]).range(0.02, 0.03), 0.5, sig);

{ SinOsc.ar([400, 405], mul: 0.1) }.play;// stereo
{ SinOsc.ar(400, mul: 0.1) }.play;// mono

/************************************************/
// first phrase of rydeen
(
Tempo.bpm = 142;
Pbind(\instrument, \prc,
	\dur, Pseq([0.5, 0.25, 0.25], inf),
	\amp, Pseq([0.3, 0.13, 0.13], inf)
).play;
)//play on pnly left as mono

(
//Pfxb apply effect to pattern
//Pfxb(pattern, effect)
~prcA = Pfxb(
	Pbind(\instrument, \prc,
		\dur, Pseq([0.5, 0.25, 0.25], inf),
		\amp, Pseq([0.3, 0.13, 0.13], inf)
	),
	\chorus
).play;
)



// make base sound by fitering Saw wave
(
//prepare arg freq and gate
SynthDef(\bss, {| gate=1, amp=0.1, sustain=1, freq=440 |
	var sig, env;
	env = EnvGen.kr(Env.adsr, gate, amp, 0, sustain, 2);
	sig = LFSaw.ar(freq);
	sig = RLPF.ar(sig, 1000);
	sig = Pan2.ar(sig, 0, env);
	Out.ar(0, sig);
}).add;
)

(
~bssA = Pbind(\instrument, \bss,
	\dur, Pseq([
		Pseq([1.5, 1.5, 1.5, 1.5, 1, 1, 2, 0.5, 1, 2.5, 1, 1], 1),
		Pseq([1.5, 1.5, 1.5, 1.5, 1, 1, 2, 2, 2, 1, 1], 1),
	], 1),
	\legato, 0.7,
	\amp, 0.6,
	\scale, [2, 4, 5, 7, 9, 10, 12],
	\degree, Pseq([
		Pseq([0, 1, 2, 0, 6, 2, 5, \, 4, 3, 5, 4], 1),
		Pseq([0, 1, 2, 0, 6, 2, 5, 5, 5, 5, 4], 1)
	], inf),
	\octave, Pseq([
		Pseq([3, 3, 3, 3, 2, 3, 2, 2, 2, 2, 2, 2], 1),
		Pseq([3, 3, 3, 3, 2, 3, 2, 2, 2, 2, 2], 1)
	], inf)
).play;

