package toylangs.bolog.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class BologNand extends BologNode {

    private final BologNode leftExpr;
    private final BologNode rightExpr;

    public BologNand(BologNode leftExpr, BologNode rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }


    public BologNode getLeftExpr() {
        return leftExpr;
    }

    public BologNode getRightExpr() {
        return rightExpr;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Arrays.asList(leftExpr, rightExpr);
    }

    @Override
    public <T> T accept(BologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
