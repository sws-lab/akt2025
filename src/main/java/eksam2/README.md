# Transaktsionaalne tehingukeel Traks

Traks on aritmeetilise avaldiste ja transaktsioonidega keel, kus väärtustamise (täpsemalt täitmise) tulemuseks on uus väärtuskeskkond.
Transaktsioon (_transaction_) garanteerib, et vea korral tühistatakse transaktsiooni käigus tehtud muudatused, st taastatakse transaktsioonieelne seisund.
Näiteks järgmine programm kannab _Raha_ muutujast _Y_ muutujasse _X_, aga katkestab tehingu, kui muutuja _Y_ muutuks negatiivseks:
```
Raha <- 10; 
traks { Y <- Y - Raha; check Y >= 0; X <- X + Raha }; 
Tulemus <- X
```

Keele AST klassid paiknevad _eksam2.ast_ paketis ja nende ülemklassiks on _TraksNode_:

* _TraksExpr_ — avaldised, alamklassidega:
    * _TraksNum_ — arvliteraal;
    * _TraksVar_ — muutuja;
    * _TraksBinOp_ — binaarsed operaatorid (_Add_, _Sub_, _Geq_);
* _TraksStmt_ — laused, alamklassidega:
    * _TraksAssign_ — omistamine;
    * _TraksBlock_ — lausete plokk;
    * _TraksCheck_ — kontrolllause (Javas võtmesõna _assert_);
    * _TraksAction_ — transaktsioonilause.

Klassis _TraksNode_ on staatilised abimeetodid, millega saab mugavamalt abstraktseid süntaksipuid luua.
Ülalolev lause moodustatakse järgmiselt:
```
block(
  assign("Raha", num(10)),
  action(block(
    assign("Y", sub(var("Y"), var("Raha"))),
    check(geq(var("Y"), num(0))),
    assign("X", add(var("X"), var("Raha"))))
  ),
  assign("Tulemus", var("X"))
)
```

## Alusosa: TraksEvaluator

Klassis _TraksEvaluator_ tuleb implementeerida meetod _eval_, mis täidab lause etteantud väärtuskeskkonnas.
Väärtustamisele ja täitmisele kehtivad järgmised nõuded:

1. Literaalid, muutujad ja aritmeetilised operaatorid (liitmine _Add_ ja lahutamine _Sub_) käituvad standardselt.
2. Võrdlusoperaator (suurem-või-võrdne _Geq_) käitub standardselt, kuid tagastab tõeväärtuse täisarvuna: 0 tähistab väära ja 1 tähistab tõest.
3. Etteantud lause täitmise tulemuseks on uus väärtuskeskkond, mitte üksik väärtus.
4. Omistamised ja plokid käituvad standardselt.
5. Kontrolllause käitub nagu Java _assert_. Kui kontrollitav tingimus on tõene (väärtus erineb 0-st), siis ei tehta midagi, aga kui kontrollitav tingimus on väär (väärtus on 0), siis katkestatakse hetkel kõige sisemine transaktsioonilause, st täitmine jätkub selle transaktsioonilause järelt.
6. Transaktsiooni katkestamisel tühistatakse kõik selle transaktsiooni käigus tehtud omistamised, st taastatakse enne transaktsiooni olnud väärtuskeskkond.
7. Kui pole ühtegi ümbritsevat transaktsioonilauset, mida katkestada, siis katkestatakse kogu programm, st täitmine lõpeb ja taastatakse algne väärtuskeskkond.
8. Defineerimata muutuja väärtustamisel või omistamisel visatakse _TraksException_.

> **PS!** Seda saab lahendada ühe visitoriga, mis avaldiste puhul arvutab väärtusi ja lausete puhul modifitseerib väljaspool olevat väärtuskeskkonda.

Transaktsioonide kohta tasub uurida teste.
Näiteks ülaltoodud programmi täitmisel alustades väärtuskeskkonnaga _[X → 1; Y → 2; Raha → 1000; Tulemus → 0]_ on tulemuseks väärtuskeskkond _[X → 1; Y → 2; Raha → 10; Tulemus → 1]_, sest tehing katkestatakse.
Kui selle programmi esimene rida oleks `Raha <- 10;` asemel `Raha <- 1;`, siis tulemuseks oleks väärtuskeskkond _[X → 2; Y → 1; Raha → 1; Tulemus → 2]_, sest tehing läheb läbi.

## Põhiosa: TraksAst

Failis _Traks.g4_ tuleb implementeerida grammatika ja klassis _TraksAst_ tuleb implementeerida meetod _parseTreeToAst_, mis teisendab parsepuu AST-iks.
Süntaksile kehtivad järgmised nõuded:

1. Arvuliteraalid koosnevad numbritest. Esimene number tohib olla 0 ainult siis, kui see on arvu ainuke number.
2. Muutuja koosneb ühest ladina suurtähest, millele järgnevad ladina väiketähed (mida võib olla ka null tükki).
3. Binaarsed operaatorid on liitmine (`+`), lahutamine (`-`) ja suurem-või-võrdne (`>=`). Aritmeetilised operaatorid on vasakassotsiatiivsed, võrdlusoperaator pole assotsiatiivne. Nende puhul kehtivad tehete standardsed prioriteedid, mis kahanevas järjekorras on: liitmine/lahutamine (sama prioriteediga), suurem-või-võrdne.
4. Avaldistes võib kasutada sulge, mis on kõige kõrgema prioriteediga.
5. Omistamine koosneb muutuja nimest, noolest (`<-`) ja avaldisest.
6. Lausete jada koosneb lausetest (mida võib olla ka null tükki), mis on omavahel eraldatud semikoolonitega (`;`). Peale viimast lauset semikoolonit pole.
7. Plokk koosneb loogelistest sulgudest (`{` `}`), mille vahel on lausete jada. 
8. Transaktsioonilause koosneb võtmesõnast `traks` ja lausest.
9. Kontrolllause koosneb võtmesõnast `check` ja avaldisest.
10. Lisaks on lubatud tingimuslause, mis koosneb lausest, võtmesõnast `when` ja avaldisest. Tingimuslause tuleb AST-is esitada olemasolevate konstruktsioonide kaudu, kasutades samaväärsust: `s when e` ≡ `traks { check e; s }`
11. Programm koosneb lausete jadast, mis tuleb AST-is esitada plokina.
12. Tühisümboleid (tühikud, tabulaatorid, reavahetused) tuleb ignoreerida.

## Lõviosa: TraksCompiler

Klassis _TraksCompiler_ tuleb implementeerida meetod _compile_, mis kompileerib lause CMa programmiks.
Kompileerimisele kehtivad järgmised nõuded:

1. Muutujate väärtused antakse _stack_'il etteantud järjekorras.
2. Programmi täitmise lõpuks peavad _stack_'il olema ainult muutujate väärtused (etteantud järjekorras), mis vastavad täitmisjärgsele väärtuskeskkonnale ja on samad nagu _TraksEvaluator_-iga täites.
3. Defineerimata muutuja väärtustamisel või omistamisel visatakse _TraksException_ **kompileerimise ajal**.
