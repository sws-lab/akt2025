package toylangs.imp.ast;

public abstract class ImpAstVisitor<T> {

    protected abstract T visit(ImpNum num);
    protected abstract T visit(ImpNeg neg);
    protected abstract T visit(ImpAdd add);
    protected abstract T visit(ImpDiv div);

    protected abstract T visit(ImpVar var);
    protected abstract T visit(ImpAssign assign);
    protected abstract T visit(ImpProg prog);

    public T visit(ImpNode node) {
        return node.accept(this);
    }
}
