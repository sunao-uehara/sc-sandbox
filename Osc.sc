s.boot;
s.isKindOf(Server);

(
f = {|aNumber|
	var anotherNumber;
	anotherNumber = aNumber + 1;
	aNumber * anotherNumber;
};
)
f.value(3);

f = { SinOsc.ar(freq: 880, mul: 0.5) * 0.1 };
f.play;

{ BrownNoise.ar * 0.1 }.play;
//{ WhiteNoise.ar * 0.5 }.play;

{ SinOsc.ar(XLine.kr(440, 5000, 2), mul: 0.1) }.play;

// modulate freq
{ SinOsc.ar(XLine.kr(2000, 200), 0, 0.5) }.play;

// modulate freq
{ SinOsc.ar(SinOsc.ar(XLine.kr(1, 1000, 9), 0, 200, 800), 0, 0.25) }.play;

// modulate phase
{ SinOsc.ar(800, SinOsc.ar(XLine.kr(1, 1000, 9), 0, 2pi), 0.25) }.play;