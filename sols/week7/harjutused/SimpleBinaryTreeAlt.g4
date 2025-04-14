grammar SimpleBinaryTreeAlt;
@header { package week7.harjutused; }

// __Kahendpuu variatsioon__
// Edasi modifitseeri oma grammatikat, et see defineeriks sellised puud,
// millel on väärtused sisetippudes, näiteks järgmine sõna:
//   Node(null, 1, Node(null, 0, null))
//
// - Arvudeks võtame endiselt 0 ja 1.
// - Konstant null on pisike puu.
// - Kui v on arv ning l ja r on puud, siis Node(l,v,r) on samuti puu.

// eeldame, et tühikuid ja tabulaatoreid võib olla igalpool suvaline arv

// Ära seda reeglit ümber nimeta, selle kaudu testitakse grammatikat
init : tree EOF;

tree
    :   Node '(' tree ',' Arv ',' tree ')'
    |   Null
    ;

Arv : [01];
Null : 'null';
Node : 'Node';

WS: [ \t]+ -> skip;
