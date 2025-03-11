package week4.baselangs.bool.ast;

public class BoolOr extends BoolNode {

    public BoolOr(BoolNode e1, BoolNode e2) {
        super(e1, e2);
    }

    public BoolNode getLeft() {
        return getChild(0);
    }

    public BoolNode getRight() {
        return getChild(1);
    }

    @Override
    public <T> T accept(BoolVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
