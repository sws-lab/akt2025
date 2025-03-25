package proovieksam.ast.operaatorid;

import proovieksam.ast.EstologNode;
import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public abstract class EstologBinOp extends EstologNode {
    private final EstologNode leftChild;
    private final EstologNode rightChild;

    public EstologBinOp(EstologNode leftChild, EstologNode rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public EstologNode getLeftChild() {
        return leftChild;
    }

    public EstologNode getRightChild() {
        return rightChild;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Arrays.asList(leftChild, rightChild);
    }
}
