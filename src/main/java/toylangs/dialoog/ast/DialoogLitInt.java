package toylangs.dialoog.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class DialoogLitInt extends DialoogNode {
    private final int value;

    public DialoogLitInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(dataNode(value));
    }

    @Override
    protected Object getNodeInfo() {
        return "num";
    }

    @Override
    public <T> T accept(DialoogAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
