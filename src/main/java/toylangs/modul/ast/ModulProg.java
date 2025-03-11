package toylangs.modul.ast;

import com.google.common.base.Preconditions;
import toylangs.AbstractNode;

import java.util.List;

public class ModulProg extends ModulNode {
    private final ModulExpr expr;
    private final int modulus;

    public ModulProg(ModulExpr expr, int modulus) {
        Preconditions.checkArgument(modulus >= 1, "modulus is less than 1: %s", modulus);
        this.expr = expr;
        this.modulus = modulus;
    }

    public ModulExpr getExpr() {
        return expr;
    }

    public int getModulus() {
        return modulus;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return List.of(expr, dataNode(modulus));
    }

    @Override
    public <T> T accept(ModulAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
