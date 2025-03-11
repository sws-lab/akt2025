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
