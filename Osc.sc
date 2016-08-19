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

f = { SinOsc.ar * 0.1 };
f.play;