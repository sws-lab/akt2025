package week9;

import week7.ast.*;


public class AktkTypeChecker {

    public static void check(AstNode ast) {
        // Meetod peab viskama RuntimeException-i (või mõne selle alamklassi erindi), kui:
        // 1) programmis kasutatakse deklareerimata muutujaid või funktsioone,
        // mida pole defineeritud ei antud programmis ega "standardteegis"
        //    (vt. interpretaatori koduülesannet)
        // 2) programmis kasutatakse mõnd lihttüüpi, mis pole ei String ega Integer
        // 3) leidub muutuja deklaratsioon, milles pole antud ei tüüpi ega algväärtusavaldist
        // 4) programmis on mõnel avaldisel vale tüüp
        throw new UnsupportedOperationException();
    }
}
