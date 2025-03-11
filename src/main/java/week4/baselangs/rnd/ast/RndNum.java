package week4.baselangs.rnd.ast;

public class RndNum extends RndNode {
    private final int value;

    public RndNum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public <T> T accept(RndVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
