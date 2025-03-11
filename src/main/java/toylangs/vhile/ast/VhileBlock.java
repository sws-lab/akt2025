package toylangs.vhile.ast;

import toylangs.AbstractNode;

import java.util.List;

public class VhileBlock extends VhileStmt {
    private final List<VhileStmt> stmts;

    public VhileBlock(List<VhileStmt> stmts) {
        this.stmts = stmts;
    }

    public List<VhileStmt> getStmts() {
        return stmts;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return stmts;
    }

    @Override
    public <T> T accept(VhileAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
