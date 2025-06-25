package eksam3.ast;

import toylangs.AbstractNode;

import java.util.List;

import static java.util.Collections.singletonList;

public class SimpNum extends SimpNode {
    private final int value;

    public SimpNum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return singletonList(dataNode(value));
    }

    @Override
    public <T> T accept(SimpAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
