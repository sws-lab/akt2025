package toylangs.safdi.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class SafdiVar extends SafdiNode {
    private final String name;

    public SafdiVar(String name) {
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
    public <T> T accept(SafdiAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
