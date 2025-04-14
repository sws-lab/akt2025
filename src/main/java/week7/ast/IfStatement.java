package week7.ast;

import java.util.Arrays;
import java.util.List;

/**
 * if-lause.
 */
public class IfStatement extends Statement {

    private final Expression condition;
    private final Block thenBranch;
    private final Block elseBranch;

    public IfStatement(Expression condition, Block thenBranch, Block elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public Expression getCondition() {
        return condition;
    }

    public Block getThenBranch() {
        return thenBranch;
    }

    public Block getElseBranch() {
        return elseBranch;
    }

    @Override
    public List<Object> getChildren() {
        return Arrays.asList(condition, thenBranch, elseBranch);
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
