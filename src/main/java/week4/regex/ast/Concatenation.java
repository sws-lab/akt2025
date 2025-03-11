package week4.regex.ast;

public class Concatenation extends RegexNode {

    public Concatenation(RegexNode left, RegexNode right) {
        super('.', left, right);
    }

    public RegexNode getLeft() {
        return getChild(0);
    }

    public RegexNode getRight() {
        return getChild(1);
    }

    @Override
    public <R> R accept(RegexVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
