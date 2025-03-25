package proovieksam.ast.operaatorid;

import proovieksam.ast.EstologAstVisitor;
import proovieksam.ast.EstologNode;

public class EstologVoi extends EstologBinOp {
    public EstologVoi(EstologNode leftChild, EstologNode rightChild) {
        super(leftChild, rightChild);
    }

    @Override
    public <T> T accept(EstologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
