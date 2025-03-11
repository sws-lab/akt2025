package week4.baselangs.bool.ast;

public abstract class BoolVisitor<T> {

    protected abstract T visit(BoolImp node);
    protected abstract T visit(BoolOr node);
    protected abstract T visit(BoolNot node);
    protected abstract T visit(BoolVar node);

    public final T visit(BoolNode node) {
        return node.accept(this);
    }

}
