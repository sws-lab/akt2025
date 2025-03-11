package toylangs.imp.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public abstract class ImpNode extends AbstractNode {

    // Võimaldavad natuke mugavamalt luua neid objekte:
    public static ImpNum num(int value) {
        return new ImpNum(value);
    }

    public static ImpNeg neg(ImpNode expr) {
        return new ImpNeg(expr);
    }

    public static ImpAdd add(ImpNode left, ImpNode right) {
        return new ImpAdd(left, right);
    }

    public static ImpDiv div(ImpNode numerator, ImpNode denominator) {
        return new ImpDiv(numerator, denominator);
    }

    public static ImpVar var(char name) {
        return new ImpVar(name);
    }

    public static ImpAssign assign(char name, ImpNode expr) {
        return new ImpAssign(name, expr);
    }

    public static ImpProg prog(ImpNode expr, List<ImpAssign> assigns) {
        return new ImpProg(expr, assigns);
    }

    public static ImpProg prog(ImpNode expr, ImpAssign... assigns) {
        return new ImpProg(expr, Arrays.asList(assigns));
    }

    // Visitor ja listener implementatsiooniks:
    public abstract <T> T accept(ImpAstVisitor<T> visitor);
}
