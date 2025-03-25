package proovieksam.ast.aatomid;

import proovieksam.ast.EstologAstVisitor;
import proovieksam.ast.EstologNode;
import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class EstologLiteraal extends EstologNode {
    private final boolean value;

    public EstologLiteraal(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    protected Object getNodeInfo() {
        return "lit";
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(dataNode(value));
    }

    @Override
    public <T> T accept(EstologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
