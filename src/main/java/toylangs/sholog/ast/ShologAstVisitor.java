package toylangs.sholog.ast;

public abstract class ShologAstVisitor<T> {

    protected abstract T visit(ShologLit lit);
    protected abstract T visit(ShologVar var);
    protected abstract T visit(ShologError error);

    protected abstract T visit(ShologEager eager);
    protected abstract T visit(ShologLazy lazy);

    public final T visit(ShologNode node) {
        return node.accept(this);
    }
}
