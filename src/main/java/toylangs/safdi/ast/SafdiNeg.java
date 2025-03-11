package toylangs.safdi.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class SafdiNeg extends SafdiNode {
    private final SafdiNode expr;

    public SafdiNeg(SafdiNode expr) {
        this.expr = expr;

    }
    public SafdiNode getExpr() {
        return expr;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(expr);
    }

    @Override
    public <T> T accept(SafdiAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
