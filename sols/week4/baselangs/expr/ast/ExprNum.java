package week4.baselangs.expr.ast;

public class ExprNum extends ExprNode {
    private final int value;

    public ExprNum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int eval() {
        return value;
    }

}
