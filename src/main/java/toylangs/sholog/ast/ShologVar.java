package toylangs.sholog.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class ShologVar extends ShologNode {
    private final String name;

    public ShologVar(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(dataNode(name));
    }

    @Override
    public <T> T accept(ShologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
