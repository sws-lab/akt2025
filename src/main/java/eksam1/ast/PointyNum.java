package eksam1.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class PointyNum extends PointyNode {
    private final int value;

    public PointyNum(int value) {
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
    public <T> T accept(PointyAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
