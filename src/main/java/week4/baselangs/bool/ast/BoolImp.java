package week4.baselangs.bool.ast;

public class BoolImp extends BoolNode {

    public BoolImp(BoolNode e, BoolNode t) {
        super(e, t);
    }

    public BoolNode getAntedecent() {
        return getChild(0);
    }

    public BoolNode getConsequent() {
        return getChild(1);
    }

    @Override
    public <T> T accept(BoolVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
