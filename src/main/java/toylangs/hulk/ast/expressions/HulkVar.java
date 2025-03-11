package toylangs.hulk.ast.expressions;

import toylangs.AbstractNode;
import toylangs.hulk.ast.HulkAstVisitor;

import java.util.Collections;
import java.util.List;

// Esindab hulga nime. Kokkuleppeliselt ladina suurtähed.
public class HulkVar extends HulkExpr {
    private final Character name;

    public HulkVar(Character name) {
        this.name = name;
    }

    public Character getName() {
        return name;
    }

    @Override
    protected Object getNodeInfo() {
        return "var";
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(dataNode(name));
    }

    @Override
    public String prettyPrint() {
        return name.toString();
    }

    @Override
    public <T> T accept(HulkAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
