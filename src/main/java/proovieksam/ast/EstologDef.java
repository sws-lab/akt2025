package proovieksam.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class EstologDef extends EstologNode {
    private final String nimi;
    private final EstologNode avaldis;

    public EstologDef(String nimi, EstologNode avaldis) {
        this.nimi = nimi;
        this.avaldis = avaldis;
    }

    public String getNimi() {
        return nimi;
    }

    public EstologNode getAvaldis() {
        return avaldis;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Arrays.asList(dataNode(nimi), avaldis);
    }

    @Override
    public <T> T accept(EstologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
