package week4.baselangs.expr.ast;

public class ExprDiv extends ExprNode {
    private final ExprNode numerator;
    private final ExprNode denominator;

    public ExprDiv(ExprNode numerator, ExprNode denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public ExprNode getNumerator() {
        return numerator;
    }

    public ExprNode getDenominator() {
        return denominator;
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
