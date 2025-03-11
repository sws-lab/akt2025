package toylangs.safdi.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class SafdiDiv extends SafdiNode {
    private final SafdiNode left;
    private final SafdiNode right;
    private final SafdiNode recover;

    public SafdiDiv(SafdiNode left, SafdiNode right, SafdiNode recover) {
        this.left = left;
        this.right = right;
        this.recover = recover;
    }

    public SafdiNode getLeft() {
        return left;
    }

    public SafdiNode getRight() {
        return right;
    }

    public SafdiNode getRecover() {
        return recover;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Arrays.asList(left, right, recover);
    }

    @Override
    public <T> T accept(SafdiAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
