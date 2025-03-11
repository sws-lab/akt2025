package week4.baselangs.rec;

import week4.baselangs.rec.ast.*;

import static week4.baselangs.rec.ast.RecNode.*;

/**
 * Siinne rec keel on koopia expr keelest, kuid kasutab uusimaid Java 21 võimalusi:
 * record-id, mustrisobitus ja sealed klassid.
 * Nagu näha, siis need võimalused oluliselt lihtsustavad avaldispuude defineerimist ja nende väärtustamist.
 * Kahjuks me AKT-s edaspidi neid võimalusi veel ei kasuta.
 */
public class RecEvaluator {

    // Võrdle seda eval meetodit VisitorIntro eval meetodiga pärast IntelliJ abil switch-iks teisendamist.
    public static int eval(RecNode node) {
        return switch (node) {
            // Record-ite puhul saab kasutada mustrisobitust (pattern matching),
            // et panna vastavad väljad kohe lokaalsetesse muutujatesse,
            // ilma et peaks num.value() vms välja kutsuma.
            case RecNum(int num) -> num;
            case RecAdd(var left, var right) -> eval(left) + eval(right);
            case RecDiv(var numerator, var denominator) -> eval(numerator) / eval(denominator);
            case RecNeg(var expr) -> -eval(expr);
            // Kuna RecNode on sealed, siis kompilaator teab, et default juhtu pole vaja.
        };
    }

    public static void main(String[] args) {
        RecNode expr = div(add(num(5), add(num(3), neg(num(2)))), num(2));
        System.out.println(eval(expr)); // tulemus: 3
    }
}
