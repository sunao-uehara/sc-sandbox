p = Pseries(0, 2, 5).asStream;

p.next;
p.next;
p.next;
p.next;
p.next;

p.next;// nil after 5 times

// create stream by Array
p = Pseq([6, 5, 1, 2.5, -100], inf).asStream;//play infinitely by inf keyword

20.do { p.next.postln; };//number.do {} executes the func 20 times


(
SynthDef(\hat, {| amp=0.1, pan=0 |
	var sig, env;
	env = EnvGen.kr(Env.perc(0, 0.03), 1, amp, doneAction: 2);
	sig = WhiteNoise.ar;
	sig = HPF.ar(sig, 8000);
	sig = Pan2.ar(sig, pan, env);
	Out.ar(0, sig);
}).add;

~hatA = Pbind(\instrument, \hat, \dur, 0.25, \amp, Pseq([0.4, 0.2, 0.3, 0.2], inf));
~hatA.play;

Tempo.bpm;
Tempo.bpm = 120;

~hatA.stop;