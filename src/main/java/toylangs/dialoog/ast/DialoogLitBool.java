package toylangs.dialoog.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class DialoogLitBool extends DialoogNode {
    private final boolean value;

    public DialoogLitBool(boolean value) {
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
    protected Object getNodeInfo() {
        return "tv";
    }

    @Override
    public <T> T accept(DialoogAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
