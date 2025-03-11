package toylangs.safdi.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class SafdiMul extends SafdiNode {
    private final SafdiNode left;
    private final SafdiNode right;

    public SafdiMul(SafdiNode left, SafdiNode right) {
        this.left = left;
        this.right = right;
    }

    public SafdiNode getLeft() {
        return left;
    }

    public SafdiNode getRight() {
        return right;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Arrays.asList(left, right);
    }

    @Override
    public <T> T accept(SafdiAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
