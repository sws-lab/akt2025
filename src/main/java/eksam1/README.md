# Pointeritega viitamise keel Pointy

Pointy on aritmeetilise avaldiste ja viitadega keel, kus avaldise väärtustamise tulemuseks on uus väärtuskeskkond.
Igal muutujal on eeldefineeritud aadress (mälupesa number), mida esitatakse täisarvuga (muutuja indeks etteantud järjestuses).
Keeles on avaldised muutuja aadressi võtmiseks (_address-of_) ja aadressil oleva väärtuse leidmiseks (_dereference_).
Näiteks järgmine programm salvestab muutuja `foo` aadressi muutujasse `x` ja muutuja `bar` aadressi muutujasse `y` 
ning siis liidetakse muutujate `x` ja `y` kaudu need kokku:
```
x = &foo, y = &bar, z = *x + *y
```

See programm käitub täpselt nagu `foo + bar` (AST-is esitatud kui `foo - (-bar)`).
Aadressidega saab teha ka aritmeetikat, näiteks `z = *(x-1)`, mille tulemuseks muutuja väärtus, mille mälupesa on enne muutujat `x`.
Nii väärtustamisel kui ka kompileerimisel on muutujate järjekord argumendina antud — neist esimese aadress on 0, teise aadress on 1, jne.

Keele AST klassid paiknevad _eksam1.ast_ paketis ja nende ülemklassiks on _PointyNode_:

* _PointyNum_ — arvliteraal;
* _PointyVar_ — muutuja;
* _PointyNeg_ — vastandarvu võtmine (unaarne miinus);
* _PointySub_ — lahutamine (binaarne miinus);
* _PointyAddrOf_ — muutuja aadressi võtmise avaldis (_address-of_);
* _PointyDeref_ — aadressil oleva väärtuse leidmise avaldis (_dereference_);
* _PointyAssign_ — omistamisavaldis;
* _PointyComma_ — komaavaldis (avaldiste jada).

Klassis _PointyNode_ on staatilised abimeetodid, millega saab mugavamalt abstraktseid süntaksipuid luua.
Ülalolev lause moodustatakse järgmiselt:
```
comma(
   assign("x", addrOf("foo")),
   assign("y", addrOf("bar")),
   assign("z", sub(
                  deref(var("x")), 
                  neg(deref(var("y"))))
               )
)
```

## Alusosa: PointyEvaluator

Klassis _PointyEvaluator_ tuleb implementeerida meetod _eval_, mis väärtustab avaldise etteantud väärtuskeskkonnas ja muutujate järjestusega.
Väärtustamisele kehtivad järgmised nõuded:

1. Literaalid, muutujad ja aritmeetilised operaatorid (unaarne ja binaarne miinus) käituvad standardselt. Kusjuures lahutamise argumendid väärtustatakse vasakult paremale.
2. Omistamisavaldis teostab omistamise (standardselt) ja selle enda väärtuseks on omistatud väärtus.
3. Etteantud avaldise väärtustamise tulemuseks on uus väärtuskeskkond, mitte üksik lõppväärtus.
4. _Address-of_ avaldise väärtuseks on vastava muutuja aadress (indeks etteantud muutujate järjestuses).
5. _Dereference_ avaldise väärtuseks on vastaval aadressil (indeksil etteantud muutujate järjestuses) oleva muutuja väärtus.
6. Komaavaldise avaldised väärtustatakse vasakult paremale ja selle enda väärtuseks on viimase avaldise väärtus.
7. Defineerimata muutuja väärtustamisel, aadressi võtmisel (_address-of_) või omistamisel visatakse _PointyException_.
8. Mitte-eksisteeriva aadressi väärtuse leidmisel (_dereference_) visatakse _PointyException_.
9. Sama alamavaldist ei väärtustata mitu korda. Näiteks `*(x = x + 1)` väärtustamine suurendab muutuja `x` väärtust täpselt ühe võrra (mitte kahe või kolme võrra).

> **PS.** Kuigi _eval_ meetod tagastab avaldise väärtustamise järgse keskkonna, siis visitor võiks tagastada avaldise väärtuse.

## Põhiosa: PointyAst

Failis _Pointy.g4_ tuleb implementeerida grammatika ja klassis _PointyAst_ tuleb implementeerida meetod _parseTreeToAst_, mis teisendab parsepuu AST-iks.
Süntaksile kehtivad järgmised nõuded:

1. Arvuliteraalid koosnevad numbritest. Esimene number tohib olla 0 ainult siis, kui see on arvu ainuke number.
2. Muutuja koosneb vähemalt ühest ladina tähest (suured ja väiksed).
3. Unaarsed (prefiks-)operaatorid on vastandarvu võtmine (`-`) ja _dereference_ (`*`) ning binaarne operaator on lahutamine (`-`). Binaarne operaator on vasakassotsiatiivne.
4. _Address-of_ avaldis koosneb ampersandist (`&`) ja muutuja nimest.
5. Omistamisavaldis koosneb muutuja nimest, võrdusmärgist (`=`) ja avaldisest.
6. Komaavaldis koosneb avaldistest (mida peab olema vähemalt kaks), mis on omavahel eraldatud komadega (`,`). Peale viimast avaldist koma pole.
7. Lisaks on lubatud binaarse operaatorina liitmine (`+`), mis on samuti vasakassotsiatiivne. Liitmine tuleb AST-is esitada olemasolevate konstruktsioonide kaudu, kasutades samaväärsust: `x + y` ≡ `x - (-y)`
8. Tehete prioriteedid kahanevas järjekorras on: (samas alampunktis olevad tehted on sama prioriteediga)
    - vastandarvu võtmine/_address-of_/_dereference_,
    - liitmine/lahutamine,
    - omistamine,
    - koma.
9. Avaldistes võib kasutada sulge, mis on kõige kõrgema prioriteediga.
10. Tühisümboleid (tühikud, tabulaatorid, reavahetused) tuleb ignoreerida.

> **NB!** ANTLRi vasakrekursiooni elimineerimise maagia nõuab, et binaarne operatsioon algaks ja lõpeks rekursiivsete kutsetega (`expr … expr`). Komaavaldised otseselt sellisel kujul ei ole, seega lõpuni lahendamiseks võiks kasutada kihilist avaldisgrammatikat.

## Lõviosa: PointyCompiler

Klassis _PointyCompiler_ tuleb implementeerida meetod _compile_, mis kompileerib avaldise CMa programmiks.
Kompileerimisele kehtivad järgmised nõuded:

1. Muutujate väärtused antakse _stack_'il etteantud järjekorras, mis ühtlasi määrab ka muutujate aadressi.
2. Avaldise veatu väärtustamise lõpuks peavad _stack_'il olema ainult muutujate väärtused (etteantud järjekorras), mis vastavad täitmisjärgsele väärtuskeskkonnale ja on samad nagu _PointyEvaluator_-iga täites. Etteantud avaldise lõppväärtust tuleb ignoreerida.
3. Defineerimata muutuja väärtustamisel, aadressi võtmisel (_address-of_) või omistamisel visatakse _PointyException_ **kompileerimise ajal**.
4. Mitte-eksisteeriva aadressi väärtuse leidmisel (_dereference_) lõpetatakse programmi töö koheselt (`HALT` instruktsioon). Sel juhul peavad _stack_'ile alles jääma ka vahetulemused, sh mitte-eksisteeriv aadress _stack_'i tipus.
