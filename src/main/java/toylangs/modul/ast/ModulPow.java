package toylangs.modul.ast;

import com.google.common.base.Preconditions;
import toylangs.AbstractNode;

import java.util.List;

public class ModulPow extends ModulExpr {
    private final ModulExpr base;
    private final int power;

    public ModulPow(ModulExpr base, int power) {
        Preconditions.checkArgument(power >= 0, "power is negative: %s", power);
        this.base = base;
        this.power = power;
    }

    public ModulExpr getBase() {
        return base;
    }

    public int getPower() {
        return power;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return List.of(base, dataNode(power));
    }

    @Override
    public <T> T accept(ModulAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
