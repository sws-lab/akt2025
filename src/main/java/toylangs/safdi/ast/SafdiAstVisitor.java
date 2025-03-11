package toylangs.safdi.ast;

public abstract class SafdiAstVisitor<T> {

    protected abstract T visit(SafdiNum num);
    protected abstract T visit(SafdiVar var);
    protected abstract T visit(SafdiNeg neg);
    protected abstract T visit(SafdiAdd add);
    protected abstract T visit(SafdiMul mul);
    protected abstract T visit(SafdiDiv div);

    public final T visit(SafdiNode node) {
        return node.accept(this);
    }
}
