package toylangs.vhile.ast;

import toylangs.AbstractNode;

import java.util.List;

public class VhileLoop extends VhileStmt {
    private final VhileExpr condition;
    private final VhileStmt body;

    public VhileLoop(VhileExpr condition, VhileStmt body) {
        this.condition = condition;
        this.body = body;
    }

    public VhileExpr getCondition() {
        return condition;
    }

    public VhileStmt getBody() {
        return body;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return List.of(condition, body);
    }

    @Override
    public <T> T accept(VhileAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
