package toylangs.safdi.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class SafdiNum extends SafdiNode {
    private final int value;

    public SafdiNum(int value) {
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
    public <T> T accept(SafdiAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
