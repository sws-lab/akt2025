package eksam1.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class PointyVar extends PointyNode {
    private final String name;

    public PointyVar(String name) {
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
    public <T> T accept(PointyAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
