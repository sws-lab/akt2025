package eksam1.ast;

import toylangs.AbstractNode;

import java.util.List;

public class PointyComma extends PointyNode {
    private final List<PointyNode> exprs;

    public PointyComma(List<PointyNode> exprs) {
        this.exprs = exprs;
    }

    public List<PointyNode> getExprs() {
        return exprs;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return exprs;
    }

    @Override
    public <T> T accept(PointyAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
