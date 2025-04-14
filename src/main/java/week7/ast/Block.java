package week7.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lausete jada.
 *
 * Kasutatakse nii terve programmi kui ka loogelistes sulgudes olevate lausete jadade tähistamiseks.
 */
public class Block extends Statement {

    private final List<Statement> statements;

    public Block(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return Collections.unmodifiableList(statements);
    }

    @Override
    public List<Object> getChildren() {
        return new ArrayList<>(statements);
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
