# Šokeerivate loogikaoperaatoritega Sholog

Sholog on tõeväärtustega arvutamise keel, kus saab lisaks visata erindeid ja kasutada laiskasid (_short-circuit_) loogikaoperaatoreid.

```
~b && (T \/ E1) + (foo || E2 /\ F)
```

## AST
Keele AST klassid paiknevad _toylangs.sholog.ast_ paketis ja nende ülemklassiks on _ShologNode_:

* _ShologLit_ – tõeväärtusliteraal;
* _ShologVar_ – muutuja;
* _ShologError_ – vea-avaldis koos täisarvulise veakoodiga (erind);
* _ShologEager_ – agaralt väärtustatavad binaarsed operaatorid (_And_, _Or_, _Xor_);
* _ShologLazy_ – laisalt väärtustatavad binaarsed operaatorid (_And_, _Or_).

Klassis _ShologNode_ on staatilised abimeetodid, millega saab mugavamalt abstraktseid süntaksipuid luua. Ülalolev avaldis moodustatakse järgmiselt:
```
xor(land(xor(var("b"), lit(true)), eor(lit(true), error(1))), lor(var("foo"), eand(error(2), lit(false))))
```

## Alusosa: ShologEvaluator
Klassis _ShologEvaluator_ tuleb implementeerida meetod _eval_, mis väärtustab tõeväärtusavaldise etteantud väärtuskeskkonnas. Väärtustamisele kehtivad järgmised nõuded:

1. Literaalid ja muutujad käituvad standardselt.
2. Vea-avaldise väärtustamisel visatakse sama veakoodiga _ShologException_.
3. Defineerimata muutuja väärtustamisel visatakse _ShologException_ koodiga 127.
4. Agarad operaatorid käituvad standardselt, kusjuures alati väärtustatakse mõlemad argumendid vasakult paremale.  
   Näiteks `eand(lit(false), error(1))` viskab väärtustamisel erindi koodiga 1.
5. Laisad operaatorid käituvad standardselt, kuid teine argument väärtustatakse ainult siis, kui esimese argumendi väärtus ei määra juba tulemust ära (_short-circuit_ väärtustamine).  
   Näiteks `land(lit(false), error(1))` ei viska väärtustamisel erindit, vaid tagastab false.

## Põhiosa: ShologAst
Failis _Sholog.g4_ tuleb implementeerida grammatika ja klassis _ShologAst_ tuleb implementeerida meetod _parseTreeToAst_, mis teisendab parsepuu AST-iks. Süntaksile kehtivad järgmised nõuded:

1. Tõene literaal on `T` ja väär literaal `F`.
2. Muutuja koosneb vähemalt ühest ladina väiketähest.
3. Vea-avaldis koosneb sümbolist `E` ja sellele järgnevast täisarvust, mis on selle veakood. Sümboli `E` ja täisarvu vahel ei ole lubatud tühisümbolid.
4. Binaarsed operaatorid koosnevad kahest avaldisest, mille vahel on operaator: agar ja laisk _And_ on vastavalt `/\` ja `&&`, _Xor_ on `+`, agar ja laisk _Or_ on vastavalt `\/` ja `||`. Kõik on vasakassotsiatiivsed ja kahanevas järjekorras on nende prioriteedid: _And_, _Xor_, _Or_.
5. Lisaks on lubatud unaarne eitusoperaator `~`, millele järgneb avaldis. Eitusoperaatori prioriteet on kõigist binaarsetest operaatoritest kõrgem ning see tuleb AST-is esitada olemasolevate konstruktsioonide kaudu, kasutades samaväärsust: `~x = x + T`.
6. Avaldistes võib kasutada sulge, mis on kõige kõrgema prioriteediga.
7. Tühisümboleid (tühikud, tabulaatorid, reavahetused) tuleb ignoreerida.

## Lõviosa: ShologCompiler
Klassis _ShologCompiler_ tuleb implementeerida meetod _compile_, mis kompileerib tõeväärtusavaldise CMa programmiks. Kompileerimisele kehtivad järgmised nõuded:

1. Tõeväärtused teisendatakse täisarvudeks _CMaUtils.bool2int_ abil.
2. Muutujate väärtused antakse stack’il etteantud järjekorras.
3. Programmi täitmise lõpuks peab stack’i pealmine element olema avaldise väärtus, mis on sama nagu _ShologEvaluator_-iga väärtustades.
4. Programmi veatu täitmise lõpuks tohivad stack’il olla ainult etteantud muutujate algsed väärtused ja arvutatud avaldise väärtus.
5. Erindi viskamiseks lisatakse stack’i peale veakoodi vastandarv ja lõpetatakse programmi töö koheselt (`HALT` instruktsioon). Sel juhul peavad stack’ile alles jääma ka vahetulemused.  
   Näiteks `eand(lit(false), error(1))` lõpetab töö stack’iga [0, -1].
6. Defineerimata muutuja korral programm kompileerub (st. ei viska kompileerimise ajal erindit), kuid lõpetab töö eelmises punktis kirjeldatud viisil, kasutades veakoodi 127.  
   Näiteks `eand(lit(false), var("jama"))` lõpetab töö stack’iga [0, -127].
