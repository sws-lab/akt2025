package toylangs.parm.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class ParmPlus extends ParmNode {

    private final ParmNode leftExpr;
    private final ParmNode rightExpr;

    public ParmPlus(ParmNode leftExpr, ParmNode rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    public ParmNode getLeftExpr() {
        return leftExpr;
    }

    public ParmNode getRightExpr() {
        return rightExpr;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Arrays.asList(leftExpr, rightExpr);
    }

    @Override
    public <T> T accept(ParmAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
