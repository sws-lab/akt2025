package toylangs.imp.ast;

import toylangs.AbstractNode;

import java.util.List;

import static java.util.Collections.singletonList;

public class ImpNeg extends ImpNode {
    private final ImpNode expr;

    public ImpNeg(ImpNode expr) {
        this.expr = expr;
    }

    public ImpNode getExpr() {
        return expr;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return singletonList(expr);
    }

    @Override
    public <T> T accept(ImpAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
