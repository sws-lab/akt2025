package toylangs.modul.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class ModulNum extends ModulExpr {
    private final int value;

    public ModulNum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(dataNode(value));
    }

    @Override
    public <T> T accept(ModulAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
