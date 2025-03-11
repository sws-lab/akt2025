package toylangs.hulk.ast.expressions;

import toylangs.AbstractNode;
import toylangs.hulk.ast.HulkAstVisitor;

import java.util.Arrays;
import java.util.List;

// Esindab tehet kahe hulga vahel. Lubatud on:
// '+'  ühend
// '&'  ühisosa
// '-'  vahe
public class HulkBinOp extends HulkExpr {
    private final Character op;
    private final HulkExpr left;
    private final HulkExpr right;

    public HulkBinOp(Character op, HulkExpr left, HulkExpr right) {
        if (op == '+' || op == '&' || op == '-') this.op = op;
        else throw new IllegalArgumentException(op.toString());
        this.left = left;
        this.right = right;
    }

    public Character getOp() {
        return op;
    }

    public HulkExpr getLeft() {
        return left;
    }

    public HulkExpr getRight() {
        return right;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Arrays.asList(AbstractNode.dataNode(op), left, right);
    }

    @Override
    public String prettyPrint() {
        return "(" + getLeft().prettyPrint() + op.toString() + getRight().prettyPrint() + ")";
    }

    @Override
    public <T> T accept(HulkAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
