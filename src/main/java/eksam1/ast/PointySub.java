package eksam1.ast;

import toylangs.AbstractNode;

import java.util.List;

public class PointySub extends PointyNode {
    private final PointyNode left;
    private final PointyNode right;

    public PointySub(PointyNode left, PointyNode right) {
        this.left = left;
        this.right = right;
    }

    public PointyNode getLeft() {
        return left;
    }

    public PointyNode getRight() {
        return right;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return List.of(left, right);
    }

    @Override
    public <T> T accept(PointyAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
