# Nulliga jagamist salliv Safdi

Safdi on aritmeetiliste avaldiste keel, millele on lisatud võimalus nulliga jagamise korral toibuda ja vea asemel väärtustada teine avaldis, mida siin nimetame taastumisavaldiseks (_recover expression_). Näiteks avaldise `x + y / z recover 1` väärtustamise tulemuseks on _x + 1_, kui _z = 0_; muidu on see lihtsalt _x + y / z_.

## AST
Keele AST klassid paiknevad _toylangs.safdi.ast_ paketis ja nende ülemklassiks on _SafdiNode_:

* _SafdiNum_ – arvliteraal;
* _SafdiVar_ – muutuja;
* _SafdiNeg_ – vastandarvu võtmine (unaarne miinus);
* _SafdiAdd_, _SafdiMul_ – liitmise ja korrutamise binaarsed operaatorid;
* _SafdiDiv_ – jagamise operaator (nulliga jagamise taastumisavaldisega).

Klassis _SafdiNode_ on staatilised abimeetodid, millega saab mugavamalt abstraktseid süntaksipuid luua. Ülalolev avaldis moodustatakse järgmiselt:
```
add(var("x"), div(var("y"), var("z"), num(1)))
```

## Alusosa: SafdiEvaluator
Klassis _SafdiEvaluator_ tuleb implementeerida meetod _eval_, mis väärtustab avaldise etteantud väärtuskeskkonnas. Väärtustamisele kehtivad järgmised nõuded:

1. Literaalid, muutujad ja kõik aritmeetilised operaatorid käituvad standardselt. Keele tuumaks on tavalised aritmeetilised avaldised.
2. Defineerimata muutuja väärtustamisel visatakse _SafdiException_.
3. Kui jagamisel on nimetaja väärtus null ja taastumisavaldist ei ole, siis tuleb samuti visata _SafdiException_.
4. Kui jagamisel on nimetaja väärtus null, aga taastumisavaldis on olemas, siis tuleb tagastada taastumisavaldise väärtus.
5. Kui nimetaja on null, siis tulemus ei sõltu lugejast. Kui nimetaja ei ole null, siis tulemus ei sõltu taastumisavaldisest. Neid ei tohiks väärtustada ja nendes esinevad vead ei tohiks väärtustamist segada, näiteks `viga/0 recover 1` tulemuseks on 1.

## Põhiosa: SafdiAst
Failis _Safdi.g4_ tuleb implementeerida grammatika ja klassis _SafdiAst_ tuleb implementeerida meetod _parseTreeToAst_, mis teisendab parsepuu AST-iks. Süntaksile kehtivad järgmised nõuded:

1. Arvuliteraalid koosnevad numbritest. Esimene number tohib olla 0 ainult siis, kui see on arvu ainuke number.
2. Muutuja koosneb vähemalt ühest ladina tähest (suured ja väiksed).
3. Aritmeetilised operaatorid on unaarne miinus (`-`), liitmine (`+`), korrutamine (`*`) ja jagamine (`/`). Nende puhul kehtivad aritmeetiliste tehete standardsed prioriteedid ja assotsiatiivsused.
4. Jagamisele (ja ainult jagamisele) võib järgneda võtmesõna `recover` ja taastumisavaldis. Oluline on taastumisavaldise korral ka säilitada operaatorite prioriteedid ja assotsiatiivsused (vt. näited allpool ja meie testides).
5. Avaldistes võib kasutada sulge, mis on kõige kõrgema prioriteediga.
6. Tühisümboleid (tühikud, tabulaatorid, reavahetused) tuleb ignoreerida.

Siin on taastumisavaldisega prioriteetide ja assotsiatiivsuste kohta paar näidet, kus on paremal nendega samaväärne (s.t. sama süntakspuuga) avaldis
```
x / y / z recover 0		(x / y) / z recover 0
x / y recover 0 / z		(x / y recover 0) / z
```
See tähendab ka seda, et `x/y*z` järel ei tohi olla taastumisavaldis, aga `x/(x*z)` järel võib!

> **NB!** ANTLRi vasakrekursiooni elimineerimise maagia nõuab, et binaarne operatsioon algaks ja lõpeks rekursiivsete kutsetega (`expr … expr`). Taastumisavaldisega jagamine otseselt sellisel kujul ei ole. Meil õnnestus see lõpuni lahendada ainult kihilise avaldisgrammatikana.

## Lõviosa: SafdiCompiler
Klassis _SafdiCompiler_ tuleb implementeerida meetod _compile_, mis kompileerib avaldise CMa programmiks. Kompileerimisele kehtivad järgmised nõuded:

1. Muutujate väärtused antakse stack’il etteantud järjekorras.
2. Programmi täitmise lõpuks peab stack’i pealmine element olema avaldise väärtus, mis on sama nagu _SafdiEvaluator_-iga väärtustades.
3. Programmi veatu täitmise lõpuks tohivad stack’il olla ainult etteantud muutujate algsed väärtused ja arvutatud avaldise väärtus.
4. Vea korral lõpetatakse programmi töö koheselt (`HALT` instruktsioon). Sel juhul peavad stack’ile alles jääma ka vahetulemused. Nulliga jagamine tuleb avastada enne lugeja väärtustamist, seega `add(num(5), div(num(2), num(0)))` korral lõpetab töö stack’iga, kuhu on lisatud arv 5 (aga mitte 2).

> **PS.** Jagamise juures võib olla vajalik jagajat kaks korda väärtustada – see on okei.
