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

## Põhiosa: VhileAst

Failis *Vhile.g4* tuleb implementeerida grammatika ja klassis *VhileAst* tuleb implementeerida meetod *parseTreeToAst*, mis teisendab parsepuu AST-iks.
Süntaksile kehtivad järgmised nõuded:

1.  Arvuliteraalid koosnevad numbritest. Esimene number tohib olla 0 ainult siis, kui see on arvu ainuke number. Nullist erinev arvuliteraal võib alata miinusmärgiga (`-`), tähistamaks negatiivset väärtust.
2.  Muutuja koosneb vähemalt ühest ladina väiketähest.
3.  Binaarsed operaatorid on liitmine (`+`), korrutamine (`*`), võrdus (`==`) ja mittevõrdus (`!=`). Aritmeetilised operaatorid on vasakassotsiatiivsed, võrdlusoperaatorid pole assotsiatiivsed. Nende puhul kehtivad tehete standardsed prioriteedid, mis kahanevas järjekorras on: korrutamine, liitmine, võrdus/mittevõrdus (sama prioriteediga).
4.  Avaldistes võib kasutada sulge, mis on kõige kõrgema prioriteediga.
5.  Omistamine koosneb muutuja nimest, võrdusmärgist (`=`) ja avaldisest.
6.  Plokk koosneb loogelistest sulgudest (`{` `}`), mille vahel on lausete jada. Lausete jada koosneb lausetest (mida võib olla ka null tükki), mis on omavahel eraldatud semikoolonitega (`;`). Peale viimast lauset semikoolonit pole.
7.  Korduslause koosneb võtmesõnast `while`, sulgudes avaldisest ja lausest.
8.  Katkestamislause koosneb võtmesõnast `escape`, millele võib järgneda koolon (`:`) ja positiivne arv. Kui viimased puuduvad, siis on see sama mis `escape:1`.
9.  Tühisümboleid (tühikud, tabulaatorid, reavahetused) tuleb ignoreerida.

## Lõviosa: VhileCompiler

Klassis *VhileCompiler* tuleb implementeerida meetod *compile*, mis kompileerib lause CMa programmiks.
Kompileerimisele kehtivad järgmised nõuded:

1.  Muutujate väärtused antakse *stack*'il etteantud järjekorras.
2.  Programmi täitmise lõpuks peavad *stack*'il olema ainult muutujate väärtused (etteantud järjekorras), mis vastavad täitmisjärgsele väärtuskeskkonnale ja on samad nagu *VhileEvaluator*-iga täites.
3.  Defineerimata muutuja väärtustamisel või omistamisel visatakse *VhileException* **kompileerimise ajal**.
