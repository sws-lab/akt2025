package week4.regex;

import week4.regex.ast.*;

public class RegexPrettyPrinter {

    // Ülesanne on ilusasti (see tähendab võimalikult väheste sulgudega) väljastada regulaaravaldised.
    // Vaata aritmeetiliste avaldiste (expr) ilutrükki klassis ExprMaster.
    public static String pretty(RegexNode regex) {
        return regex.toString();
    }
}
