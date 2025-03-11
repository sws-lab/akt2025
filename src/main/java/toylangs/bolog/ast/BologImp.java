package toylangs.bolog.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class BologImp extends BologNode {

    private final List<BologNode> assumptions;
    private final BologNode conclusion;

    public BologImp(BologNode conclusion, List<BologNode> assumptions) {
        this.assumptions = assumptions;
        this.conclusion = conclusion;
    }

    public BologImp(BologNode conclusion, BologNode... assumptions) {
        this(conclusion, Arrays.asList(assumptions));
    }

    public List<BologNode> getAssumptions() {
        return assumptions;
    }

    public BologNode getConclusion() {
        return conclusion;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return AbstractNode.cons(conclusion, assumptions);
    }

    @Override
    public <T> T accept(BologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
