package eksam1.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class PointyNeg extends PointyNode {
    private final PointyNode expr;

    public PointyNeg(PointyNode expr) {
        this.expr = expr;
    }

    public PointyNode getExpr() {
        return expr;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(expr);
    }

    @Override
    public <T> T accept(PointyAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
