package eksam2.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class TraksCheck extends TraksStmt {
    private final TraksExpr expr;

    public TraksCheck(TraksExpr expr) {
        this.expr = expr;
    }

    public TraksExpr getExpr() {
        return expr;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(expr);
    }

    @Override
    public <T> T accept(TraksAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
