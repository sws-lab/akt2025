package week4.baselangs.expr.ast;

public class ExprNeg extends ExprNode {

    private final ExprNode expr;

    public ExprNeg(ExprNode expr) {
        this.expr = expr;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int eval() {
        return 0;
    }


}
