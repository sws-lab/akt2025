package toylangs.bolog.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class BologLit extends BologNode {
    private final boolean value;

    public BologLit(boolean value) {
        this.value = value;
    }

    public boolean isTrue() {
        return value;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(dataNode(value));
    }

    @Override
    protected Object getNodeInfo() {
        return "tv";
    }

    @Override
    public <T> T accept(BologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
