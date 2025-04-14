package week7.ast;

/**
 * Täisarvuliteraal
 */
public class IntegerLiteral extends Literal<Integer> {

    public IntegerLiteral(Integer value) {
        super(value);
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
