package toylangs.safdi.ast;

import toylangs.AbstractNode;

public abstract class SafdiNode extends AbstractNode {

    public static SafdiNum num(int value) {
        return new SafdiNum(value);
    }

    public static SafdiVar var(String name) {
        return new SafdiVar(name);
    }

    public static SafdiNeg neg(SafdiNode expr) {
        return new SafdiNeg(expr);
    }

    public static SafdiAdd add(SafdiNode left, SafdiNode right) {
        return new SafdiAdd(left, right);
    }

    public static SafdiMul mul(SafdiNode left, SafdiNode right) {
        return new SafdiMul(left, right);
    }

    public static SafdiDiv div(SafdiNode left, SafdiNode right) {
        return div(left, right, null);
    }

    public static SafdiDiv div(SafdiNode left, SafdiNode right, SafdiNode recover) {
        return new SafdiDiv(left, right, recover);
    }


    public abstract <T> T accept(SafdiAstVisitor<T> visitor);
}
