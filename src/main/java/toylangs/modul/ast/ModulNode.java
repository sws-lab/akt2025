package toylangs.modul.ast;

import toylangs.AbstractNode;

public abstract class ModulNode extends AbstractNode {

    public static ModulNum num(int value) {
        return new ModulNum(value);
    }

    public static ModulVar var(String name) {
        return new ModulVar(name);
    }

    public static ModulNeg neg(ModulExpr expr) {
        return new ModulNeg(expr);
    }

    public static ModulAdd add(ModulExpr left, ModulExpr right) {
        return new ModulAdd(left, right);
    }

    public static ModulExpr sub(ModulExpr left, ModulExpr right) {
        return add(left, neg(right));
    }

    public static ModulMul mul(ModulExpr left, ModulExpr right) {
        return new ModulMul(left, right);
    }

    public static ModulPow pow(ModulExpr base, int power) {
        return new ModulPow(base, power);
    }

    public static ModulProg prog(ModulExpr expr, int modulus) {
        return new ModulProg(expr, modulus);
    }


    public abstract <T> T accept(ModulAstVisitor<T> visitor);
}
