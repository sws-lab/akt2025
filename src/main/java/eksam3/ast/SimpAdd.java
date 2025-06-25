package eksam3.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class SimpAdd extends SimpNode {
    private final SimpNode left;
    private final SimpNode right;

    public SimpAdd(SimpNode left, SimpNode right) {
        this.left = left;
        this.right = right;
    }

    public SimpNode getLeft() {
        return left;
    }

    public SimpNode getRight() {
        return right;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Arrays.asList(left, right);
    }

    @Override
    public <T> T accept(SimpAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
