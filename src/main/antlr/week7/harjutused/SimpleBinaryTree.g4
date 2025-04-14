grammar SimpleBinaryTree;
@header { package week7.harjutused; }

// __Kahendpuu__
// Oletame, et kahendpuu lehtedes on ainult arvud 0 ja 1, siis defineeri keel,
// kus on kõik võimalikud puud.
// Konstruktorite nimed on Leaf(int v) ja Node(Tree left, Tree right)
// ehk teiste sõnadega defineerime puude hulga järgmiselt:
//
// - Arvudeks on meil 0 ja 1.
// - Kui v on arv, siis Leaf(v) on puu.
// - Kui l ja r on puud, siis Node(l,r) on samuti puu.
//
// Keel sisaldab seega sõnu nagu:
//   Node(Leaf(1), Node(Leaf(0), Leaf(1)))

// eeldame, et tühikuid ja tabulaatoreid võib olla igalpool suvaline arv

// Ära seda reeglit ümber nimeta, selle kaudu testitakse grammatikat
init : 'implementeeri mind!' EOF;  // siit peab grammatika algama
