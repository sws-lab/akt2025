package toylangs.modul.ast;

import toylangs.AbstractNode;

import java.util.List;

public class ModulNeg extends ModulExpr {
    private final ModulExpr expr;

    public ModulNeg(ModulExpr expr) {
        this.expr = expr;
    }

    public ModulExpr getExpr() {
        return expr;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return List.of(expr);
    }

    @Override
    public <T> T accept(ModulAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
