package eksam3.ast;

import toylangs.AbstractNode;

import java.util.List;

import static java.util.Collections.singletonList;

public class SimpMem extends SimpNode {
    private final int loc;

    public SimpMem(int loc) {
        this.loc = loc;
    }

    public int getLoc() {
        return loc;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return singletonList(dataNode(loc));
    }

    @Override
    public <T> T accept(SimpAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
