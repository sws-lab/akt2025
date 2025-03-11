package toylangs.vhile.ast;

import toylangs.AbstractNode;

import java.util.List;

public class VhileAssign extends VhileStmt {
    private final String name;
    private final VhileExpr expr;

    public VhileAssign(String name, VhileExpr expr) {
        this.name = name;
        this.expr = expr;
    }

    public String getName() {
        return name;
    }

    public VhileExpr getExpr() {
        return expr;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return List.of(dataNode(name), expr);
    }

    @Override
    public <T> T accept(VhileAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
