package week4.baselangs.bool.ast;

public class BoolNot extends BoolNode {

    public BoolNot(BoolNode e) {
        super(e);
    }

    public BoolNode getExp() {
        return getChild(0);
    }

    @Override
    public <T> T accept(BoolVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
