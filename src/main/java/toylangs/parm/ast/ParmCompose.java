package toylangs.parm.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public class ParmCompose extends ParmNode {

    private final ParmNode fst;
    private final ParmNode snd;
    private final boolean parallel;

    public ParmCompose(ParmNode fst, ParmNode snd, boolean parallel) {
        this.fst = fst;
        this.snd = snd;
        this.parallel = parallel;
    }

    public ParmNode getFst() {
        return fst;
    }

    public ParmNode getSnd() {
        return snd;
    }

    public boolean isParallel() {
        return parallel;
    }

    @Override
    protected List<AbstractNode> getAbstractNodeList() {
        return Arrays.asList(fst, snd);
    }

    @Override
    protected Object getNodeInfo() {
        return isParallel() ? "par" : "seq";
    }

    @Override
    public <T> T accept(ParmAstVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
