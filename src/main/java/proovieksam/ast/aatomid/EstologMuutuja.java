package proovieksam.ast.aatomid;

import proovieksam.ast.EstologAstVisitor;
import proovieksam.ast.EstologNode;
import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class EstologMuutuja extends EstologNode {
    private final String nimi;

    public EstologMuutuja(String nimi) {
        this.nimi = nimi;
    }

    public String getNimi() {
        return nimi;
    }

    @Override
    protected Object getNodeInfo() {
        return "var";
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(dataNode(nimi));
    }

    @Override
    public <T> T accept(EstologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
