package week4.regex.ast;

public class Epsilon extends RegexNode {

    public Epsilon() {
        super('ε');
    }

    @Override
    public <R> R accept(RegexVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
