# Modulaarse aritmeetika keel Modul

Modul on aritmeetiliste avaldiste keel, kus kõik arvutused tehakse fikseeritud mooduli järgi, st kõigile tulemustele rakendatakse jäägiga jagamist. Matemaatiliselt öeldes, arvutatakse jäägiklassidega. Näiteks avaldise `3 + 4 (mod 5)` väärtuseks on 2, mis saadakse, kui 7 jagatakse jäägiga 5-ga.

Keele AST klassid paiknevad *toylangs.modul.ast* paketis ja nende ülemklassiks on *ModulNode*:

-   *ModulNum* — arvliteraal;
-   *ModulVar* — muutuja;
-   *ModulNeg* — vastandarvu võtmine (unaarne miinus);
-   *ModulAdd*, *ModulMul* — liitmise ja korrutamise binaarsed operaatorid;
-   *ModulPow* — naturaalarvuga astendamise operaator;
-   *ModulProg* — terviklik programm, mis koosneb avaldisest ja konstantsest moodulist.

Klassis *ModulNode* on staatilised abimeetodid, millega saab mugavamalt abstraktseid süntaksipuid luua. 
Ülalolev avaldis moodustatakse järgmiselt:

```
prog(add(num(3), num(4)), 5)
```

## Alusosa: ModulEvaluator

Klassis *ModulEvaluator* tuleb implementeerida meetod *eval*, mis väärtustab programmi etteantud väärtuskeskkonnas. 
Väärtustamisele kehtivad järgmised nõuded:

1.  Literaalid, muutujad ja kõik aritmeetilised operaatorid käituvad standardselt.
2.  Defineerimata muutuja väärtustamisel visatakse *ModulException*.
3.  Programmi väärtuseks on avaldise väärtus antud mooduli järgi.
    Mooduli *m* järgi on lubatud väärtused 0 kuni *m - 1* (mõlemad kaasa arvatud).
4.  Kõik vahetulemused peavad olema samuti antud mooduli järgi, et ei tekiks aritmeetilist *overflow*-d, 
    näiteks programmis `2^100 (mod 7)`.

> **NB!** Arvu *x* väärtuse mooduli *m* järgi saab arvutada valemiga *((x % m) + m) % m*, kus *%* tähistab Java jäägi operaatorit. Lihtsalt valemist *x % m* ei piisa, sest see ei anna negatiivsete väärtustega soovitud tulemust.
> Alustuseks võid aga lihtsalt väärtustada avaldise osa: nelja esimese testi jaoks ei mängi modulaarne aritmeetika mingit rolli.

## Põhiosa: ModulAst

Failis *Modul.g4* tuleb implementeerida grammatika ja klassis *ModulAst* tuleb implementeerida meetod *parseTreeToAst*, mis teisendab parsepuu AST-iks. 
Süntaksile kehtivad järgmised nõuded:

1.  Arvuliteraalid koosnevad numbritest. Esimene number tohib olla 0 ainult siis, kui see on arvu ainuke number.
2.  Muutuja koosneb vähemalt ühest ladina väiketähest, millele võivad lõpus järgneda alakriipsud (`_`).
3.  Aritmeetilised operaatorid on unaarne miinus (`-`), liitmine (`+`), lahutamine (`-`), korrutamine (`*`) ja astendamine (`^`). 
    Nende puhul kehtivad aritmeetiliste tehete standardsed assotsiatiivsused ja prioriteedid, mis kahanevas järjekorras on: 
    astendamine, unaarne miinus, korrutamine, liitmine/lahutamine (sama prioriteediga).
4.  Lahutamine tuleb AST-is esitada olemasolevate konstruktsioonide kaudu, kasutades samaväärsust: *x - y = x + (-y)*.
5.  Programm koosneb avaldisest ning sulgudes võtmesõnast `mod`, tühikust ja naturaalarvust.
6.  Avaldistes võib kasutada sulge, mis on kõige kõrgema prioriteediga.
7.  Tühisümboleid (tühikud, tabulaatorid, reavahetused) tuleb ignoreerida, välja arvatud ülal nõutud.

## Lõviosa: ModulCompiler

Klassis *ModulCompiler* tuleb implementeerida meetod *compile*, mis kompileerib programmi CMa programmiks. 
Kompileerimisele kehtivad järgmised nõuded:

1.  Muutujate väärtused antakse *stack*'il etteantud järjekorras.
2.  Programmi täitmise lõpuks peab *stack*'i pealmine element olema avaldise väärtus, mis on sama nagu *ModulEvaluator*-iga väärtustades.
3.  Programmi täitmise lõpuks tohivad *stack*'il olla ainult etteantud muutujate algsed väärtused ja arvutatud avaldise väärtus.
4.  Defineerimata muutuja kasutamisel visatakse *ModulException* **kompileerimise ajal**.

> **PS.** Kuna astendajad on konstantsed, siis astendamise saab kompileerida `DUP` ja `MUL` instruktsioonide jadaks.

## Meistriosa: ModulMaster

Klassis *ModulMaster* tuleb implementeerida meetodid *eval* ja *compile*, mis on täpselt nagu alusosa ja lõviosa, kuid lisaks saavad efektiivselt hakkama väga suurte astendajatega.

> **PS.** Kasuta kiire astendamise algoritmi (<http://kodu.ut.ee/~ahto/eio/2012.03.24/matem.pdf>).
