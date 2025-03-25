package proovieksam.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class EstologKui extends EstologNode {
    private final EstologNode kuiAvaldis;
    private final EstologNode siisAvaldis;
    private final EstologNode muiduAvaldis;

    public EstologKui(EstologNode kuiAvaldis, EstologNode siisAvaldis, EstologNode muiduAvaldis) {
        this.kuiAvaldis = kuiAvaldis;
        this.siisAvaldis = siisAvaldis;
        this.muiduAvaldis = muiduAvaldis;
    }

    public EstologKui(EstologNode kuiAvaldis, EstologNode siisAvaldis) {
        this(kuiAvaldis, siisAvaldis, null);
    }

    public EstologNode getKuiAvaldis() {
        return kuiAvaldis;
    }

    public EstologNode getSiisAvaldis() {
        return siisAvaldis;
    }

    public EstologNode getMuiduAvaldis() {
        return muiduAvaldis;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Arrays.asList(kuiAvaldis, siisAvaldis, muiduAvaldis);
    }

    @Override
    public <T> T accept(EstologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
