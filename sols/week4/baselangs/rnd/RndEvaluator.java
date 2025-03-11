package week4.baselangs.rnd;

import week4.baselangs.rnd.ast.*;

import java.util.function.BooleanSupplier;

public class RndEvaluator {

    // Väärtusta antud avaldist vasakult paremale, kasutades ettantud münti.
    public static int eval(RndNode expr, BooleanSupplier coin) {
        return new RndVisitor<Integer>() {
            @Override
            protected Integer visit(RndNum num) {
                return num.getValue();
            }

            @Override
            protected Integer visit(RndNeg neg) {
                return -visit(neg.getExpr());
            }

            @Override
            protected Integer visit(RndFlip flip) {
                return coin.getAsBoolean() ? visit(flip.getLeft()) : visit(flip.getRight());
            }

            @Override
            protected Integer visit(RndAdd add) {
                return visit(add.getLeft()) + visit(add.getRight());
            }
        }.visit(expr);
    }
}
