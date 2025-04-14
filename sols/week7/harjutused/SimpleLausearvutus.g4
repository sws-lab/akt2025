grammar SimpleLausearvutus;
@header { package week7.harjutused; }

// __Lausearvutuse valem__
// Eeldades, et lausemuutujateks on meil X, Y ja Z, siis saame sõnu nagu näiteks:
//      (¬(X&Y)→(X∨Z))

// Ole ettevaatlik et kasutad õigeid sümboleid:
//      ¬ & ∨ → ↔

// Ära seda reeglit ümber nimeta, selle kaudu testitakse grammatikat
init : valem EOF;

valem
    :   Ident
    |   '¬' valem
    |   '(' valem ('&' | '∨' | '→' | '↔') valem ')'
    ;

Ident : [XYZ];
