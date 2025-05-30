package eksam2.ast;

import toylangs.AbstractNode;

import java.util.List;

public class TraksBlock extends TraksStmt {
    private final List<TraksStmt> stmts;

    public TraksBlock(List<TraksStmt> stmts) {
        this.stmts = stmts;
    }

    public List<TraksStmt> getStmts() {
        return stmts;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return stmts;
    }

    @Override
    public <T> T accept(TraksAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
