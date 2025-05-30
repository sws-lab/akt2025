package eksam1.ast;

import toylangs.AbstractNode;

import java.util.List;

public abstract class PointyNode extends AbstractNode {

    public static PointyNum num(int value) {
        return new PointyNum(value);
    }

    public static PointyVar var(String name) {
        return new PointyVar(name);
    }

    public static PointyNeg neg(PointyNode expr) {
        return new PointyNeg(expr);
    }

    public static PointySub sub(PointyNode left, PointyNode right) {
        return new PointySub(left, right);
    }


    public static PointyAddrOf addrOf(String name) {
        return new PointyAddrOf(name);
    }

    public static PointyDeref deref(PointyNode expr) {
        return new PointyDeref(expr);
    }


    public static PointyAssign assign(String name, PointyNode expr) {
        return new PointyAssign(name, expr);
    }

    public static PointyComma comma(List<PointyNode> exprs) {
        return new PointyComma(exprs);
    }

    public static PointyComma comma(PointyNode... exprs) {
        return comma(List.of(exprs));
    }


    public abstract <T> T accept(PointyAstVisitor<T> visitor);
}
