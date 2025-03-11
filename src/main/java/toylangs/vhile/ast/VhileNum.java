package toylangs.vhile.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class VhileNum extends VhileExpr {
    private final int value;

    public VhileNum(int value) {
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
    public <T> T accept(VhileAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
