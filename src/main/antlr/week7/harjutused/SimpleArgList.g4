grammar SimpleArgList;
@header { package week7.harjutused; }

// __Argumentide list__
// peab ära tundma kõik sellised funktsiooniväljakutsed: f(), f(w), f(w, w), f(w, w, w)
// eeldame, et muutujanimed on alati w ja funktsiooninimed on f.
// eeldame, et tühikuid ja tabulaatoreid võib olla igalpool suvaline arv.

// Ära seda reeglit ümber nimeta, selle kaudu testitakse grammatikat
init : 'implementeeri mind!' EOF;  // siit peab grammatika algama
