package toylangs.dialoog.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class DialoogVar extends DialoogNode {
    private final String name;

    public DialoogVar(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(dataNode(name));
    }

    @Override
    public <T> T accept(DialoogAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
