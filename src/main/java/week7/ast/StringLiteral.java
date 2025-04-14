package week7.ast;

/**
 * Sõneliteraal.
 */
public class StringLiteral extends Literal<String> {

    public StringLiteral(String value) {
        super(value);
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
