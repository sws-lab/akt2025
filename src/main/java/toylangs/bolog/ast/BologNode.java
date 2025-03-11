package toylangs.bolog.ast;

import toylangs.AbstractNode;

import java.util.List;

public abstract class BologNode extends AbstractNode {

    // Literaalid ja muutuja kasutus
    public static BologNode tv(boolean value) { return new BologLit(value); }
    public static BologNode var(String name) { return new BologVar(name); }

    // Implikatsioon
    public static BologNode imp(BologNode conclusion, List<BologNode> assumptions) {
        return new BologImp(conclusion, assumptions);
    }
    public static BologNode imp(BologNode conclusion, BologNode... assumptions) {
        return new BologImp(conclusion, assumptions);
    }

    // Nand
    public static BologNode nand(BologNode left, BologNode right) { return new BologNand(left, right); }
    public static BologNode not(BologNode expr) { return nand(tv(true), expr); }
    public static BologNode and(BologNode left, BologNode right) { return not(nand(left, right)); }
    public static BologNode or(BologNode left, BologNode right) { return nand(not(left), not(right)); }
    public static BologNode xor(BologNode left, BologNode right) { return and(or(left,right), nand(left,right)); }

    public abstract <T> T accept(BologAstVisitor<T> visitor);
}
