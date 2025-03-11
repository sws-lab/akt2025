package toylangs.parm.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class ParmVar extends ParmNode {
    private final String name;

    public ParmVar(String name) {
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
    public <T> T accept(ParmAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
