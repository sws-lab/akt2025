package toylangs.sholog.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public abstract class ShologBinary extends ShologNode {
    private final ShologNode left;
    private final ShologNode right;

    public ShologBinary(ShologNode left, ShologNode right) {
        this.left = left;
        this.right = right;
    }

    public ShologNode getLeft() {
        return left;
    }

    public ShologNode getRight() {
        return right;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Arrays.asList(left, right);
    }
}
