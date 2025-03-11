package toylangs.imp.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class ImpAssign extends ImpNode {
    private final char name;
    private final ImpNode expr;

    public ImpAssign(char name, ImpNode expr) {
        this.name = name;
        this.expr = expr;
    }

    public char getName() {
        return name;
    }

    public ImpNode getExpr() {
        return expr;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Arrays.asList(dataNode(name), expr);
    }

    @Override
    public <T> T accept(ImpAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
