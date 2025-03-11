package toylangs.sholog.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class ShologLit extends ShologNode {
    private final boolean value;

    public ShologLit(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(dataNode(value));
    }

    @Override
    public <T> T accept(ShologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
