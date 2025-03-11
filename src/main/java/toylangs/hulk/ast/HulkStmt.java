package toylangs.hulk.ast;

import toylangs.AbstractNode;
import toylangs.hulk.ast.expressions.HulkExpr;

import java.util.Arrays;
import java.util.List;

// Lause esindab hulgale mingi väärtuse omistamist.
// Koosneb hulga nimest (Character), avaldisest ning tingimusest. Tingimus (cond) võib olla null.
// Lauset ei täideta kui tingimus on väär.
public class HulkStmt extends HulkNode {

    private final Character name;
    private final HulkExpr expr;
    private final HulkCond cond;

    public HulkStmt(Character name, HulkExpr expr, HulkCond cond) {
        this.name = name;
        this.expr = expr;
        this.cond = cond;
    }

    public Character getName() {
        return name;
    }

    public HulkExpr getExpr() {
        return expr;
    }

    public HulkCond getCond() {
        return cond;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Arrays.asList(dataNode(name), expr, cond);
    }

    @Override
    public String prettyPrint() {
        String result = getName().toString() + " := " + getExpr().prettyPrint();
        if (getCond() != null)
            result += " | " + getCond().prettyPrint();
        return result;
    }

    @Override
    public <T> T accept(HulkAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
