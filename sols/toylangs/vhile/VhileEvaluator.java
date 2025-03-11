package toylangs.vhile;

import cma.CMaUtils;
import toylangs.vhile.ast.*;

import java.util.HashMap;
import java.util.Map;

public class VhileEvaluator {

    public static Map<String, Integer> eval(VhileStmt stmt, Map<String, Integer> initialEnv) {
        Map<String, Integer> env = new HashMap<>(initialEnv); // teeme koopia, sest initialEnv pole muudetav

        try {
            // lausetel väärtust pole, aga avaldistel on
            new VhileAstVisitor<Integer>() {
                @Override
                protected Integer visit(VhileNum num) {
                    return num.getValue();
                }

                @Override
                protected Integer visit(VhileVar var) {
                    Integer value = env.get(var.getName());
                    if (value == null)
                        throw new VhileException();
                    else
                        return value;
                }

                @Override
                protected Integer visit(VhileBinOp binOp) {
                    int left = visit(binOp.getLeft());
                    int right = visit(binOp.getRight());
                    return switch (binOp.getOp()) {
                        case Add -> left + right;
                        case Mul -> left * right;
                        case Eq -> CMaUtils.bool2int(left == right);
                        case Neq -> CMaUtils.bool2int(left != right);
                    };
                }

                @Override
                protected Integer visit(VhileAssign assign) {
                    if (env.containsKey(assign.getName())) {
                        env.put(assign.getName(), visit(assign.getExpr()));
                        return null;
                    }
                    else
                        throw new VhileException();
                }

                @Override
                protected Integer visit(VhileBlock block) {
                    for (VhileStmt stmt : block.getStmts()) {
                        visit(stmt);
                    }
                    return null;
                }

                @Override
                protected Integer visit(VhileLoop loop) {
                    try {
                        while (CMaUtils.int2bool(visit(loop.getCondition()))) {
                            visit(loop.getBody());
                        }
                    }
                    catch (BreakException e) {
                        if (e.getLevel() > 1)
                            throw new BreakException(e.getLevel() - 1);
                    }
                    return null;
                }

                @Override
                protected Integer visit(VhileEscape escape) {
                    throw new BreakException(escape.getLevel());
                }
            }.visit(stmt);
        }
        catch (BreakException ignored) {
            // liiga suur escape level, lõpetab kogu programmi töö erindita
        }
        return env;
    }

    /**
     * Erind, mille viskamise ja püüdmise abil saab katkestamislauseid käsitleda.
     */
    private static class BreakException extends RuntimeException {
        private final int level;

        public BreakException(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }
}
