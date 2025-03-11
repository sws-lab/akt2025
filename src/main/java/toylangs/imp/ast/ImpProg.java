package toylangs.imp.ast;

import toylangs.AbstractNode;

import java.util.List;

public class ImpProg extends ImpNode {
    private final ImpNode expr;
    private final List<ImpAssign> assigns;

    public ImpProg(ImpNode expr, List<ImpAssign> assigns) {
        this.assigns = assigns;
        this.expr = expr;
    }

    public ImpNode getExpr() {
        return expr;
    }

    public List<ImpAssign> getAssigns() {
        return assigns;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return AbstractNode.cons(expr, assigns);
    }

    @Override
    public <T> T accept(ImpAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
