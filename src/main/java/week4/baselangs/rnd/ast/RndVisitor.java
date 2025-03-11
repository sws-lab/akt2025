package week4.baselangs.rnd.ast;

public abstract class RndVisitor<T> {

    protected abstract T visit(RndNum num);
    protected abstract T visit(RndNeg neg);
    protected abstract T visit(RndFlip flip);
    protected abstract T visit(RndAdd add);

    public T visit(RndNode node) {
        return node.accept(this);
    }
}
