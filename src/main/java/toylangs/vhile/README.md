# Tsükliliste konstruktsioonide keel Vhile

Vhile on aritmeetiliste avaldiste ja korduslausetega keel, kus väärtustamise (täpsemalt täitmise) tulemuseks on uus väärtuskeskkond. Näiteks järgmise programmi täitmisel alustades väärtuskeskonnaga *[x → 1; y → 5]* on tulemuseks väärtuskeskkond *[x → 120; y → 0]*.

```
while (y != 0) { 
  x = x * y;
  y = y + -1
}
```

Keele AST klassid paiknevad *toylangs.vhile.ast* paketis ja nende ülemklassiks on *VhileNode*:

-   *VhileExpr* — avaldised, alamklassidega:
    -   *VhileNum* — arvliteraal;
    -   *VhileVar* — muutuja;
    -   *VhileBinOp* — binaarsed operaatorid (*Add*, *Mul*, *Eq*, *Neq*);
-   *VhileStmt* — laused, alamklassidega:
    -   *VhileAssign* — omistamine;
    -   *VhileBlock* — lausete plokk;
    -   *VhileLoop* — korduslause (*while* tsükkel);
    -   *VhileEscape* — katkestamislause (Javas võtmesõna *break*).

Klassis *VhileNode* on staatilised abimeetodid, millega saab mugavamalt abstraktseid süntaksipuid luua.
Ülalolev lause moodustatakse järgmiselt:

```
loop(neq(var("y"), num(0)), block(
  assign("x", mul(var("x"), var("y"))), 
  assign("y", add(var("y"), num(-1)))
))
```

## Alusosa: VhileEvaluator

Klassis *VhileEvaluator* tuleb implementeerida meetod *eval*, mis täidab lause etteantud väärtuskeskkonnas. 
Väärtustamisele ja täitmisele kehtivad järgmised nõuded:

1.  Literaalid, muutujad ning aritmeetilised operaatorid (liitmine *Add* ja korrutamine *Mul*) käituvad standardselt.
2.  Võrdlusoperaatorid (võrdus *Eq* ja mittevõrdus *Neq*) käituvad ka standardselt, kuid tagastavad tõeväärtuse täisarvuna: 0 tähistab väära ja 1 tähistab tõest.
3.  Etteantud lause täitmise tulemuseks on uus väärtuskeskkond, mitte üksik väärtus.
4.  Omistamised, plokid ning korduslaused käituvad standardselt. Korduslause tingimus on väär, kui avaldise väärtus on 0, ja tõene, kui avaldise väärtus erineb 0-st.
5.  Katkestamislause käitub nagu Java *break*, aga lisaks näitab argument mitmest ümbritsevast korduslausest välja hüpatakse. Kui pole piisavalt korduslauseid, millest välja hüpata, siis lõpetatakse kogu programmi täitmine ilma erindita.
6.  Defineerimata muutuja väärtustamisel või omistamisel visatakse *VhileException*.

> **PS!** Seda saab lahendada ühe visitoriga, mis avaldiste puhul arvutab väärtusi ja lausete puhul modifitseerib väljaspool olevat väärtuskeskkonda.

Katkestamise kohta tasub uurida teste. Näiteks järgmise programmi korral on täitmise tulemuseks keskkond, kus *x*-i väärtus on 5:

```
while (x != 10) {
  x = x + 1; 
  while (x == 5) escape:2
}
```

Kui selles näites oleks `escape:2` asemel `escape:1`, siis see katkestaks ainult sisemise tsükli ja täitmise lõpuks oleks keskkonnas *x*-i väärtus 10.
