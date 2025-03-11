package toylangs.vhile.ast;

import toylangs.AbstractNode;

import java.util.List;

public class VhileBinOp extends VhileExpr {

    public enum Op {
        Add("add"),
        Mul("mul"),
        Eq("eq"),
        Neq("neq");

        private final String nodeInfo;

        Op(String nodeInfo) {
            this.nodeInfo = nodeInfo;
        }
    }

    private final Op op;
    private final VhileExpr left;
    private final VhileExpr right;

    public VhileBinOp(Op op, VhileExpr left, VhileExpr right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public Op getOp() {
        return op;
    }

    public VhileExpr getLeft() {
        return left;
    }

    public VhileExpr getRight() {
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
    public <T> T accept(VhileAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
