package toylangs.parm.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class ParmUpdate extends ParmNode {

    private final String vname;
    private final ParmNode value;

    public ParmUpdate(String vname, ParmNode value) {
        this.vname = vname;
        this.value = value;
    }

    public String getVariableName() {
        return vname;
    }

    public ParmNode getValue() {
        return value;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Arrays.asList(dataNode(vname), value);
    }

    @Override
    protected Object getNodeInfo() {
        return "up";
    }

    @Override
    public <T> T accept(ParmAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
