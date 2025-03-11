package toylangs.hulk.ast.expressions;

import toylangs.AbstractNode;
import toylangs.hulk.ast.HulkAstVisitor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Esindab elementide hulka.
// Elementideks on Characterid (kokkuleppeliselt väiketähed)
public class HulkLit extends HulkExpr {
    private final Set<Character> elements;

    public HulkLit(Set<Character> elements) {
        this.elements = elements;
    }

    public Set<Character> getElements() {
        return elements;
    }

    @Override
    protected Object getNodeInfo() {
        return "lit";
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return elements.stream().map(AbstractNode::dataNode).collect(Collectors.toList());
    }

    @Override
    protected boolean canHaveEmptyChildList() {
        return true;
    }

    @Override
    public String prettyPrint() {
        return elements.toString().replace('[', '{').replace(']', '}');
    }

    @Override
    public <T> T accept(HulkAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
