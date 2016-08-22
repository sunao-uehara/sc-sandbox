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
