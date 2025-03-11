package toylangs.modul.ast;

import toylangs.AbstractNode;

import java.util.List;

public class ModulAdd extends ModulExpr {
    private final ModulExpr left;
    private final ModulExpr right;

    public ModulAdd(ModulExpr left, ModulExpr right) {
        this.left = left;
        this.right = right;
    }

    public ModulExpr getLeft() {
        return left;
    }

    public ModulExpr getRight() {
        return right;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return List.of(left, right);
    }

    @Override
    public <T> T accept(ModulAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
