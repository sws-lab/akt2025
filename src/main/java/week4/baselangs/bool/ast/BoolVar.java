package week4.baselangs.bool.ast;

public class BoolVar extends BoolNode {
    private final char name;

    public BoolVar(char name) {
        this.name = name;
    }

    public char getName() {
        return name;
    }

    @Override
    public <T> T accept(BoolVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "var('" + name + "')";
    }
}
