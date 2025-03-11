package week4.regex.ast;

public class Repetition extends RegexNode {

    public Repetition(RegexNode child) {
        super('*', child);
    }

    public RegexNode getChild() {
        return getChild(0);
    }

    @Override
    public <R> R accept(RegexVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
