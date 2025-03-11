package toylangs.dialoog;

import toylangs.dialoog.ast.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static toylangs.dialoog.ast.DialoogNode.*;

public class DialoogEvaluator {

    public static class UndeclaredVariableException extends RuntimeException {
        public UndeclaredVariableException(String variable) {
            super("Variable \"" + variable + "\" has not been declared.");
        }
    }

    public static Object eval(DialoogNode prog, Map<String, Object> env) {
        return new DialoogAstVisitor<>() {

            final Set<String> declarations = new HashSet<>();

            @Override
            protected Object visit(DialoogLitInt num) {
                return num.getValue();
            }

            @Override
            protected Object visit(DialoogLitBool tv) {
                return tv.getValue();
            }

            @Override
            protected Object visit(DialoogVar var)  {
                if (declarations.contains(var.getName())) return env.get(var.getName());
                throw new UndeclaredVariableException(var.getName());
            }

            @Override
            protected Object visit(DialoogDecl decl) {
                declarations.add(decl.getName());
                return null;
            }

            @Override
            protected Object visit(DialoogUnary unary) {
                return unary.getOp().toJava().apply(visit(unary.getExpr()));
            }

            @Override
            protected Object visit(DialoogBinary binary) {
                Object v1 = visit(binary.getLeftExpr());
                Object v2 = visit(binary.getRightExpr());
                return binary.getOp().toJava().apply(v1, v2);
            }

            @Override
            protected Object visit(DialoogTernary ifte) {
                boolean guard = (boolean) visit(ifte.getGuardExpr());
                return guard ? visit(ifte.getTrueExpr()) : visit(ifte.getFalseExpr());
            }
        }.visit(prog);
    }

    public static void main(String[] args) {
        DialoogNode prog = prog(
                decls(iv("x"), bv("a")),
                ifte(var("a"), var("x"), il(0)));
        Map<String, Object> env = new HashMap<>();
        env.put("a", true);
        env.put("x", 7);
        System.out.println(eval(prog, env));
    }

}
