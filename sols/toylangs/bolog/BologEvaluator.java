package toylangs.bolog;

import toylangs.bolog.ast.*;

import java.util.Set;

public class BologEvaluator {

    public static boolean eval(BologNode expr, Set<String> trueVars) {
        return new BologAstVisitor<Boolean>() {
            @Override
            protected Boolean visit(BologLit tv) {
                return tv.isTrue();
            }

            @Override
            protected Boolean visit(BologVar var) {
                return trueVars.contains(var.getName());
            }

            @Override
            protected Boolean visit(BologNand nand) {
                return !(visit(nand.getLeftExpr()) && visit(nand.getRightExpr()));
            }

            @Override
            protected Boolean visit(BologImp imp) {
                return visit(imp.getConclusion()) || !imp.getAssumptions().stream().allMatch(this::visit);
            }
        }.visit(expr);
    }

}
