package toylangs.safdi;

import toylangs.safdi.ast.*;

import java.util.Map;

public class SafdiEvaluator {

    public static class SafdiException extends RuntimeException {

    }


    public static int eval(SafdiNode node, Map<String, Integer> env) {
        return new SafdiAstVisitor<Integer>() {
            @Override
            protected Integer visit(SafdiNum num) {
                return num.getValue();
            }

            @Override
            protected Integer visit(SafdiVar var) {
                Integer value = env.get(var.getName());
                if (value == null)
                    throw new SafdiException();
                else
                    return value;
            }

            @Override
            protected Integer visit(SafdiNeg neg) {
                return -visit(neg.getExpr());
            }

            @Override
            protected Integer visit(SafdiAdd add) {
                return visit(add.getLeft()) + visit(add.getRight());
            }

            @Override
            protected Integer visit(SafdiMul mul) {
                return visit(mul.getLeft()) * visit(mul.getRight());
            }

            @Override
            protected Integer visit(SafdiDiv div) {
                int right = visit(div.getRight());
                if (right == 0) {
                    if (div.getRecover() != null)
                        return visit(div.getRecover());
                    else
                        throw new SafdiException();
                }
                else
                    return visit(div.getLeft()) / right;
            }
        }.visit(node);
    }
}
