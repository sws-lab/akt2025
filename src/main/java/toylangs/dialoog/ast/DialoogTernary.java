package toylangs.dialoog.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class DialoogTernary extends DialoogNode {

    private final DialoogNode guardExpr;
    private final DialoogNode trueExpr;
    private final DialoogNode falseExpr;

    public DialoogTernary(DialoogNode guardExpr, DialoogNode trueExpr, DialoogNode falseExpr) {
        this.guardExpr = guardExpr;
        this.trueExpr = trueExpr;
        this.falseExpr = falseExpr;
    }

    public DialoogNode getGuardExpr() {
        return guardExpr;
    }

    public DialoogNode getTrueExpr() {
        return trueExpr;
    }

    public DialoogNode getFalseExpr() {
        return falseExpr;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Arrays.asList(guardExpr, trueExpr, falseExpr);
    }

    @Override
    protected Object getNodeInfo() {
        return "ifte";
    }

    @Override
    public <T> T accept(DialoogAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
