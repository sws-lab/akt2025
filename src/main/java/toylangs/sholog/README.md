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
