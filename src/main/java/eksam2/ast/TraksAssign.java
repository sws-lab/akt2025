package eksam2.ast;

import toylangs.AbstractNode;

import java.util.List;

public class TraksAssign extends TraksStmt {
    private final String name;
    private final TraksExpr expr;

    public TraksAssign(String name, TraksExpr expr) {
        this.name = name;
        this.expr = expr;
    }

    public String getName() {
        return name;
    }

    public TraksExpr getExpr() {
        return expr;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return List.of(dataNode(name), expr);
    }

    @Override
    public <T> T accept(TraksAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
