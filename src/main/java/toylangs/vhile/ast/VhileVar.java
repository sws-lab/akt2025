package toylangs.vhile.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class VhileVar extends VhileExpr {
    private final String name;

    public VhileVar(String name) {
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
    public <T> T accept(VhileAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
