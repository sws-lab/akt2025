package toylangs.sholog.ast;

import toylangs.AbstractNode;

public abstract class ShologNode extends AbstractNode {

    public static ShologLit lit(boolean value) {
        return new ShologLit(value);
    }

    public static ShologVar var(String name) {
        return new ShologVar(name);
    }

    public static ShologError error(int code) {
        return new ShologError(code);
    }

    public static ShologEager eand(ShologNode left, ShologNode right) {
        return new ShologEager(ShologEager.Op.And, left, right);
    }

    public static ShologEager eor(ShologNode left, ShologNode right) {
        return new ShologEager(ShologEager.Op.Or, left, right);
    }

    public static ShologEager xor(ShologNode left, ShologNode right) {
        return new ShologEager(ShologEager.Op.Xor, left, right);
    }

    public static ShologLazy land(ShologNode left, ShologNode right) {
        return new ShologLazy(ShologLazy.Op.And, left, right);
    }

    public static ShologLazy lor(ShologNode left, ShologNode right) {
        return new ShologLazy(ShologLazy.Op.Or, left, right);
    }

    public static ShologBinary binary(String op, ShologNode left, ShologNode right) {
        switch (op) {
            case "/\\":
                return eand(left, right);
            case "&&":
                return land(left, right);
            case "+":
                return xor(left, right);
            case "\\/":
                return eor(left, right);
            case "||":
                return lor(left, right);
        }
        throw new IllegalArgumentException("unknown op");
    }

    public abstract <T> T accept(ShologAstVisitor<T> visitor);
}
