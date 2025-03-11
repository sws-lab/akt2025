package toylangs.dialoog.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class DialoogDecl extends DialoogNode {

    private final String name;
    private final boolean intType;

    public DialoogDecl(String name, boolean intType) {
        this.name = name;
        this.intType = intType;
    }

    public String getName() {
        return name;
    }

    public boolean isIntType() {
        return intType;
    }

    @Override
    protected Object getNodeInfo() {
        return intType ? "iv" : "bv";
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(dataNode(name));
    }

    @Override
    public <T> T accept(DialoogAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
