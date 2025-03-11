package toylangs.parm.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class ParmLit extends ParmNode {
    private final int value;

    public ParmLit(int value) {
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
    public <T> T accept(ParmAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
