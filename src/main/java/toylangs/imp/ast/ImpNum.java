package toylangs.imp.ast;

import toylangs.AbstractNode;

import java.util.List;

import static java.util.Collections.singletonList;

public class ImpNum extends ImpNode {
    private final int value;

    public ImpNum(int value) {
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
    public <T> T accept(ImpAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
