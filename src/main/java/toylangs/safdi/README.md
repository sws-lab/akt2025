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
