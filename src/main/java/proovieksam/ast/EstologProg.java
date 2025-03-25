package proovieksam.ast;

import toylangs.AbstractNode;

import java.util.List;

public class EstologProg extends EstologNode {
    private final EstologNode avaldis;
    private final List<EstologDef> defs;

    public EstologProg(EstologNode avaldis, List<EstologDef> defs) {
        this.defs = defs;
        this.avaldis = avaldis;
    }

    public EstologNode getAvaldis() {
        return avaldis;
    }

    public List<EstologDef> getDefs() {
        return defs;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return AbstractNode.cons(avaldis, defs);
    }

    @Override
    public <T> T accept(EstologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
