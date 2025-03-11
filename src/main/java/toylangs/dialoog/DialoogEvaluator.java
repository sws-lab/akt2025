package toylangs.dialoog;

import toylangs.dialoog.ast.*;

import java.util.HashMap;
import java.util.Map;

import static toylangs.dialoog.ast.DialoogNode.*;

public class DialoogEvaluator {

    public static class UndeclaredVariableException extends RuntimeException {
        public UndeclaredVariableException(String variable) {
            super("Variable \"" + variable + "\" has not been declared.");
        }
    }

    public static Object eval(DialoogNode prog, Map<String, Object> env) {
        throw new UnsupportedOperationException();
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
