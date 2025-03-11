package toylangs.imp.ast;

import toylangs.AbstractNode;

import java.util.List;

import static java.util.Collections.singletonList;

public class ImpVar extends ImpNode {
    private final char name;

    public ImpVar(char name) {
        this.name = name;
    }

    public char getName() {
        return name;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return singletonList(dataNode(name));
    }

    @Override
    public <T> T accept(ImpAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
