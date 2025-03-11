package week4.baselangs.expr.ast;

public class ExprAdd extends ExprNode {
    private final ExprNode left;
    private final ExprNode right;

    public ExprAdd(ExprNode left, ExprNode right) {
        this.left = left;
        this.right = right;
    }

    public ExprNode getLeft() {
        return left;
    }

    public ExprNode getRight() {
        return right;
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
