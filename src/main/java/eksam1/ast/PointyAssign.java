package eksam1.ast;

import toylangs.AbstractNode;

import java.util.List;

public class PointyAssign extends PointyNode {
    private final String name;
    private final PointyNode expr;

    public PointyAssign(String name, PointyNode expr) {
        this.name = name;
        this.expr = expr;
    }

    public String getName() {
        return name;
    }

    public PointyNode getExpr() {
        return expr;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return List.of(dataNode(name), expr);
    }

    @Override
    public <T> T accept(PointyAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
