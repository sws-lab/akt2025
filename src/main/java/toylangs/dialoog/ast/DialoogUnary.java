package toylangs.dialoog.ast;

import java.util.Arrays;
import java.util.Map;
import java.util.function.UnaryOperator;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class DialoogUnary extends DialoogNode {

    public enum UnOp {
        DialoogNeg("-", x -> -(int)x),
        DialoogNot("!", x -> !(boolean)x);

        private final String symb;
        private final UnaryOperator<Object> javaOp;

        UnOp(String symb, UnaryOperator<Object> javaOp) {
            this.symb = symb;
            this.javaOp = javaOp;
        }

        public String getSymb() {
            return symb;
        }

        public UnaryOperator<Object> toJava() {
            return javaOp;
        }

        private final static Map<String, UnOp> symbolMap =
                Arrays.stream(UnOp.values()).collect(toMap(UnOp::getSymb, identity()));

        public static UnOp fromSymb(String symb) {
            return symbolMap.get(symb);
        }

    }

    private final UnOp op;
    private final DialoogNode expr;

    public DialoogUnary(UnOp op, DialoogNode expr) {
        this.op = op;
        this.expr = expr;
    }

    public DialoogUnary(String symb, DialoogNode expr) {
        this(UnOp.fromSymb(symb), expr);
    }

    public UnOp getOp() {
        return op;
    }

    public DialoogNode getExpr() {
        return expr;
    }

    @Override
    protected Object getNodeInfo() {
        return op.toString().substring(4).toLowerCase();
    }

    @Override
    public <T> T accept(DialoogAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
