package toylangs.imp.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class ImpAdd extends ImpNode {
    private final ImpNode left;
    private final ImpNode right;

    public ImpAdd(ImpNode left, ImpNode right) {
        this.left = left;
        this.right = right;
    }

    public ImpNode getLeft() {
        return left;
    }

    public ImpNode getRight() {
        return right;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Arrays.asList(left, right);
    }

    @Override
    public <T> T accept(ImpAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
