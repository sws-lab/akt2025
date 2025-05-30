package eksam2.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class TraksVar extends TraksExpr {
    private final String name;

    public TraksVar(String name) {
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
    public <T> T accept(TraksAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
