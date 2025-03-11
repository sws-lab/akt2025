package toylangs.hulk.ast;

import toylangs.AbstractNode;
import toylangs.hulk.ast.expressions.HulkExpr;

import java.util.Arrays;
import java.util.List;

// Tingimus esindab alamhulgaks olemist.
// Koosneb kahest avaldisest: sebset (alamhulk) ja superset (ülemhulk).
// On tõene siis, kui iga alamhulga element kuulub ka ülemhulkga.
public class HulkCond extends HulkNode {

    private final HulkExpr subset;
    private final HulkExpr superset;

    public HulkCond(HulkExpr subset, HulkExpr superset) {
        this.subset = subset;
        this.superset = superset;
    }

    public HulkExpr getSubset() {
        return subset;
    }

    public HulkExpr getSuperset() {
        return superset;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Arrays.asList(subset, superset);
    }

    @Override
    public String prettyPrint() {
        return getSubset().prettyPrint() + " subset " + getSuperset().prettyPrint();
    }

    @Override
    public <T> T accept(HulkAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
