Praktikumiülesannete lahendusi võiks paigutada siia kausta.
Nende nimed peavad olema:
* yl1.jff - Sõnad üle tähestiku {a,b}, mis sisaldavad alamsõnena "aba" ehk regulaaravaldis "(a|b)*aba(a|b)*".
* yl2.jff - Sõnad üle tähestiku {a,b}, mis sisaldavad täpselt ühe 'a'.
* yl3.jff - Sõnad üle tähestiku {a,b}, kus igale 'a'-le järgneb varem või hiljem vähemalt üks 'b'. Selline lause, nagu matemaatikas ikka, ei välista seda, et sõnes ei esine ühtegi 'a'. Keelde kuulub seega ka tühi sõne ja lihtsalt 'b'.
* yl4.jff - Sõnad üle tähestiku {a,b,x}, kus iga 'b' kõrval on 'a', s.t. kas vahetult enne või järel.
* yl5.jff - Sõnad üle tähestiku {a,b}, kus 'a' ega 'b' ei kordu, s.t. sõne on tühi või 'a' ja 'b'-d tulevad vaheldumisi.
* yl6.jff - Sõnad üle tähestiku {a,b}, mis ei sisalda alamsõne "aba". (NB! Selle lahendamine on lihtne, kui teisendada automaat deterministlikuks.)
* yl7.jff - Sõnad üle tähestiku {a,b}, mis sisaldavad alamsõnena "aba" ja kus igale 'a'-le järgneb varem või hiljem vähemalt üks 'b'. (Automaatide 1. ja 3. keelte ühisosa.)
* yl8.jff - Sõnad üle tähestiku {a,b}, mis sisaldavad alamsõne "bb".
* yl9.jff - Sõnad üle tähestiku {a,b}, mis sobituvad regulaaravaldisega "(a*b)*".
* yl10.jff - Sõnad üle tähestiku {a,b}, milles on mõlemat tähte vähemalt üks.
* yl11.jff - Sõnad üle tähestiku {a,b}, mis sobituvad regulaaravaldisega "(a*b)*" või milles on mõlemat tähte vähemalt üks. (Automaatide 9. ja 10. keelte ühend.)
* yl12.jff - Sõnad üle tähestiku {a,b}, mis sisaldavad alamsõne "bb" ja sobituvad regulaaravaldisega "(a*b)*". (Automaatide 8. ja 9. keelte ühisosa.)
