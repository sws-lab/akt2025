package toylangs.bolog.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class BologVar extends BologNode {
    private final String name;

    public BologVar(String name) {
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
    public <T> T accept(BologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
