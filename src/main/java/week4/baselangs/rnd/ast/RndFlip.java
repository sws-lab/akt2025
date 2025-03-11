package week4.baselangs.rnd.ast;

public class RndFlip extends RndNode {
    private final RndNode left;
    private final RndNode right;

    public RndFlip(RndNode left, RndNode right) {
        this.left = left;
        this.right = right;
    }

    public RndNode getLeft() {
        return left;
    }

    public RndNode getRight() {
        return right;
    }

    @Override
    public <T> T accept(RndVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
