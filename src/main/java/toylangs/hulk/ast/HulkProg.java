package toylangs.hulk.ast;

import java.util.List;
import java.util.stream.Collectors;

// Iga AST algab selle tipuga. Programm koosneb lausete listist.
public class HulkProg extends HulkNode {
    private final List<HulkStmt> statements;

    public HulkProg(List<HulkStmt> statements) {
        this.statements = statements;
    }

    public List<HulkStmt> getStatements() {
        return statements;
    }

    @Override
    protected List<HulkStmt> getAbstractNodeList() {
        return statements;
    }

    @Override
    public String prettyPrint() {
        return statements.stream().map(HulkStmt::prettyPrint).collect(Collectors.joining("\n"));
    }

    @Override
    public <T> T accept(HulkAstVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
