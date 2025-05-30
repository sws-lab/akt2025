package eksam2.ast;

import toylangs.AbstractNode;

import java.util.Collections;
import java.util.List;

public class TraksAction extends TraksStmt {
    private final TraksStmt stmt;

    public TraksAction(TraksStmt stmt) {
        this.stmt = stmt;
    }

    public TraksStmt getStmt() {
        return stmt;
    }

    @Override
    protected List<? extends AbstractNode> getAbstractNodeList() {
        return Collections.singletonList(stmt);
    }

    @Override
    public <T> T accept(TraksAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
