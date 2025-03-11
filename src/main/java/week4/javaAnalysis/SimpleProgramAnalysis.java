package week4.javaAnalysis;

import org.eclipse.jdt.core.dom.*;


public class SimpleProgramAnalysis {

    /**
     * See meetod peab ütlema (tagastama), kas antud Java programmis
     * esineb mõni ühetäheline nimi. Loeme praegu nimedeks kõiki süntaksipuu tippe,
     * mille tüüp on SimpleName.
     */
    public static boolean hasOneLetterNames(String javaSourceCode) {
        throw new UnsupportedOperationException();
    }

    /**
     * See meetod peab tagastama mitu korda esineb programmis else-võtmesõna.
     * NB! Sõneliteraalides või kommentaarides esinevaid else-sid ei loe me võtmesõnadeks
     *
     * Vihje: else saab Javas esineda vaid if-lause (IfStatement) koosseisus
     */
    public static int numberOfElseKeywords(String javaSourceCode) {
        throw new UnsupportedOperationException();
    }


    /**
     * See meetod peab tagastama täisarvu, mis näitab mitu tsüklit
     * (while, do-while või mingi for-i variant) asuvad antud programmis
     * mõne teise tsükli sees
     *
     * Vihje: Huvipakkuvad tiputüübid on WhileStatement, ForStatement,
     *           EnhancedForStatement ja DoStatement
     */
    public static int numberOfNestedLoops (String javaSourceCode) {
        throw new UnsupportedOperationException();
    }



    /**
     * See meetod peab tagastama täisarvu, mis näitab mitu muutujadeklaratsiooni
     * on antud programmis kasutud, st. mitmel juhul on (lokaalne) muutuja deklareeritud
     * ilma, et teda selles meetodis hiljem kasutataks
     *
     * Kuigi Java skoobisüsteem lubab ühes meetodis deklareerida mitu erinevat samanimelist
     * muutujat, võib praegu lihtsuse mõttes eeldada, et erinevaid samanimelisi muutujaid
     * ei esine.
     *
     * Parameetreid ega klassi- isendivälju me siin lokaalsete muutujate hulka ei loe
     * (jah, harilikult loetakse ka meetodi parameetrid lokaalseteks muutujateks, aga lihtsuse
     * mõttes praegu me neid ei arvesta).
     */
    public static int numberOfUnusedVariables(String javaSourceCode) {
        throw new UnsupportedOperationException();
    }
}
