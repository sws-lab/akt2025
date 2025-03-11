package week4.baselangs.rnd.ast;

public class RndNeg extends RndNode {
    private final RndNode expr;

    public RndNeg(RndNode expr) {
        this.expr = expr;
    }

    public RndNode getExpr() {
        return expr;
    }

    @Override
    public <T> T accept(RndVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
