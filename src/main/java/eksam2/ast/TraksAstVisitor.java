package eksam2.ast;

public abstract class TraksAstVisitor<T> {

    protected abstract T visit(TraksNum num);
    protected abstract T visit(TraksVar var);
    protected abstract T visit(TraksBinOp binop);

    protected abstract T visit(TraksAssign assign);
    protected abstract T visit(TraksBlock block);
    protected abstract T visit(TraksCheck check);
    protected abstract T visit(TraksAction action);

    public T visit(TraksNode node) {
        return node.accept(this);
    }
}
