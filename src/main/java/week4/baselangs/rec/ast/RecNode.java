package week4.baselangs.rec.ast;

// See liides on kinnine (sealed), seega RecEvaluator-is kompilaator teab, et default juhtu pole vaja.
// See on võimalik alates Java 17-st.
public sealed interface RecNode permits RecNum, RecNeg, RecAdd, RecDiv {

    // Võimaldavad natuke mugavamalt luua neid objekte:
    static RecNode num(int value) {
        return new RecNum(value);
    }

    static RecNode neg(RecNode expr) {
        return new RecNeg(expr);
    }

    static RecNode add(RecNode left, RecNode right) {
        return new RecAdd(left, right);
    }

    static RecNode div(RecNode numerator, RecNode denominator) {
        return new RecDiv(numerator, denominator);
    }
}
