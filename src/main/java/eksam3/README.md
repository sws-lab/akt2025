# Lihtne aritmeetikakeel Simp

Simp on taskuarvuti aritmeetikakeel, mille programmid koosnevad ühest või enamast avaldisest.
Avaldiste tulemused jäävad meelde ja järgnevates avaldistes saab mälupesade kaudu neile viidata, näiteks:
```
5; 
m1 + 1; 
m1 + m2;
```

Esimese avaldise tulemus on lihtsalt 5 ja selle saab teisel real kätte `m1` pesa kaudu.
Teise rea tulemus on _5+1_ ning viimasel real arvutatakse _6+5_ ehk eelmise ja üle-eelmiste avaldise summa.

> **NB!** Addresseerimine on suhteline: `m1` väärtuseks on teisel real 5 ja kolmandal real 6.

Keele AST klassid paiknevad _eksam3.ast_ paketis ja nende ülemklassiks on _SimpNode_:

* _SimpNum_ — arvliteraal;
* _SimpMem_ — mälupesa;
* _SimpAdd_, _SimpMul_ — liitmise ja korrutamise binaarsed operaatorid;
* _SimpProg_ — terviklik programm, mis koosneb avaldistest.

Klassis _SimpNode_ on staatilised abimeetodid, millega saab mugavamalt abstraktseid süntaksipuid luua. 
Ülalolev programm moodustatakse järgmiselt:
```
prog(
  num(5), 
  add(mem(1), num(1)), 
  add(mem(1), mem(2))
)
```

## Alusosa: SimpEvaluator

Klassis _SimpEvaluator_ tuleb implementeerida meetod _eval_, mis väärtustab programmi. 
Väärtustamisele kehtivad järgmised nõuded:

1. Literaalid ja aritmeetilised operaatorid käituvad standardselt.
2. Mälupesad käituvad nagu muutujad, kuid mälupesa `mem(n)` väärtuseks on _n_ sammu tagasi oleva avaldise väärtus.
3. Programmi väärtuseks on tema viimase avaldise väärtus.
4. Defineerimata mälupesa väärtustamisel visatakse _SimpException_.

## Põhiosa: SimpAst

Failis _Simp.g4_ tuleb implementeerida grammatika ja klassis _SimpAst_ tuleb implementeerida meetod _parseTreeToAst_, mis teisendab parsepuu AST-iks. 
Süntaksile kehtivad järgmised nõuded:

1. Arvuliteraalid koosnevad numbritest. Esimene number tohib olla 0 ainult siis, kui see on arvu ainuke number. Nullist erinev arvuliteraal võib alata miinusmärgiga (`-`), tähistamaks negatiivset väärtust.
2. Mälupesa algab tähega `m`, millele võib järgneda üks number vahemikus 1-9. Seega lubatud pesad on `m` ning `m1`, `m2` kuni `m9`, kusjuures `m` ja `m1` on lihtsalt sünonüümid.
3. Binaarsed operaatorid on liitmine (`+`) ja korrutamine (`*`). Nende assotsiatiivsused ja prioriteedid on standardsed.
4. Avaldistes võib kasutada sulge, mis on kõige kõrgema prioriteediga.
5. Programm koosneb ühest või enamast semikooloniga lõppevast avaldisest.
6. Tühisümboleid (tühikud, tabulaatorid, reavahetused) tuleb ignoreerida.

## Lõviosa: SimpCompiler

Klassis _SimpCompiler_ tuleb implementeerida meetod _compile_, mis kompileerib programmi CMa programmiks. 
Kompileerimisele kehtivad järgmised nõuded:

1. Programmi täitmise lõpuks peavad _stack_'il olema kõigi programmi avaldiste väärtused nende esinemise järjekorras.
2. See tähendab, et _stack_'i pealmine element peab olema programmi viimase avaldise väärtus, mis on sama nagu _SimpEvaluator_-iga väärtustades.
3. Defineerimata mälupesa väärtustamisel visatakse _SimpException_ **kompileerimise ajal**.
