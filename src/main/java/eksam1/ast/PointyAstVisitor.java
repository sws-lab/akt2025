package eksam1.ast;

public abstract class PointyAstVisitor<T> {

    protected abstract T visit(PointyNum num);
    protected abstract T visit(PointyVar var);
    protected abstract T visit(PointyNeg neg);
    protected abstract T visit(PointySub sub);

    protected abstract T visit(PointyAddrOf addrOf);
    protected abstract T visit(PointyDeref deref);

    protected abstract T visit(PointyAssign assign);
    protected abstract T visit(PointyComma comma);

    public T visit(PointyNode node) {
        return node.accept(this);
    }
}
