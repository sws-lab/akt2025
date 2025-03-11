package week4.baselangs.rnd.ast;

public abstract class RndNode {

    // Võimaldavad natuke mugavamalt luua neid objekte:
    public static RndNode num(int value) {
        return new RndNum(value);
    }

    public static RndNode neg(RndNode expr) {
        return new RndNeg(expr);
    }

    public static RndNode add(RndNode left, RndNode right) {
        return new RndAdd(left, right);
    }

    public static RndNode flip(RndNode left, RndNode right) {
        return new RndFlip(left, right);
    }

    // Visitori implementatsiooniks:
    public abstract <T> T accept(RndVisitor<T> visitor);
}
