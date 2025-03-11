package toylangs.sholog.ast;

public class ShologLazy extends ShologBinary {

    public enum Op {
        And("land"),
        Or("lor");

        private final String nodeInfo;

        Op(String nodeInfo) {
            this.nodeInfo = nodeInfo;
        }
    }

    private final Op op;

    public ShologLazy(Op op, ShologNode left, ShologNode right) {
        super(left, right);
        this.op = op;
    }

    public Op getOp() {
        return op;
    }

    @Override
    protected Object getNodeInfo() {
        return op.nodeInfo;
    }

    @Override
    public <T> T accept(ShologAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
