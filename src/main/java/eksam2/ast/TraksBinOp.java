package eksam2.ast;

import toylangs.AbstractNode;

import java.util.List;

public class TraksBinOp extends TraksExpr {

    public enum Op {
        Add("add"),
        Sub("sub"),
        Geq("geq");

        private final String nodeInfo;

        Op(String nodeInfo) {
            this.nodeInfo = nodeInfo;
        }
    }

    private final Op op;
    private final TraksExpr left;
    private final TraksExpr right;

    public TraksBinOp(Op op, TraksExpr left, TraksExpr right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public Op getOp() {
        return op;
    }

    public TraksExpr getLeft() {
        return left;
    }

    public TraksExpr getRight() {
        return right;
    }

    @Override
    protected Object getNodeInfo() {
        return op.nodeInfo;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return List.of(left, right);
    }

    @Override
    public <T> T accept(TraksAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
