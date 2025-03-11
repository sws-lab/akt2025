package toylangs.pullet.ast;

public abstract class PulletAstVisitor<T> {

    protected abstract T visit(PulletNum num);
    protected abstract T visit(PulletVar var);
    protected abstract T visit(PulletBinding binding);
    protected abstract T visit(PulletSum sum);
    protected abstract T visit(PulletDiff diff);

    public T visit(PulletNode node) {
        return node.accept(this);
    }
}
