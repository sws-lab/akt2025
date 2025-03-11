package toylangs.imp;

import toylangs.imp.ast.*;

import java.util.HashMap;
import java.util.Map;

public class ImpEvaluator {

    public static int eval(ImpProg expr) {
        return new ImpAstVisitor<Integer>() {
            private final Map<Character, Integer> env = new HashMap<>();

            @Override
            protected Integer visit(ImpNum num) {
                return num.getValue();
            }

            @Override
            protected Integer visit(ImpNeg neg) {
                return -visit(neg.getExpr());
            }

            @Override
            protected Integer visit(ImpAdd add) {
                return visit(add.getLeft()) + visit(add.getRight());
            }

            @Override
            protected Integer visit(ImpDiv div) {
                return visit(div.getNumerator()) / visit(div.getDenominator());
            }

            @Override
            protected Integer visit(ImpVar var) {
                return env.get(var.getName());
            }

            @Override
            protected Integer visit(ImpAssign assign) {
                env.put(assign.getName(), visit(assign.getExpr()));
                return null; // omistamisel pole tagastusväärtust
            }

            @Override
            protected Integer visit(ImpProg prog) {
                for (ImpAssign assign : prog.getAssigns()) {
                    visit(assign); // ignoreeri (null) tagastusväärtust
                }
                return visit(prog.getExpr());
            }
        }.visit(expr);
    }
}
