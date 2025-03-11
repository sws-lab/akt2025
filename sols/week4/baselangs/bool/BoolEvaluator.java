package week4.baselangs.bool;

import week4.baselangs.bool.ast.*;

import java.util.Set;

public class BoolEvaluator {

    // Väärtustada tõeväärtusavaldis, kui ette antud on tõeste muutujate hulk.
    public static boolean eval(BoolNode exp, Set<Character> tv) {
        return new BoolVisitor<Boolean>() {
            @Override
            protected Boolean visit(BoolImp node) {
                return !visit(node.getAntedecent()) || visit(node.getConsequent());
            }

            @Override
            protected Boolean visit(BoolOr node) {
                return visit(node.getLeft()) || visit(node.getRight());
            }

            @Override
            protected Boolean visit(BoolNot node) {
                return !visit(node.getExp());
            }

            @Override
            protected Boolean visit(BoolVar node) {
                return tv.contains(node.getName());
            }
        }.visit(exp);
    }
}
