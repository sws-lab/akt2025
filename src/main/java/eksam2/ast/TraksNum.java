package eksam2.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class TraksNum extends TraksExpr {
    private final int value;

    public TraksNum(int value) {
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
    public <T> T accept(TraksAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
