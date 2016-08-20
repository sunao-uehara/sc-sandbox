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