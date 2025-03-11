package toylangs.vhile.ast;

public abstract class VhileAstVisitor<T> {

    protected abstract T visit(VhileNum num);
    protected abstract T visit(VhileVar var);
    protected abstract T visit(VhileBinOp binOp);

    protected abstract T visit(VhileAssign assign);
    protected abstract T visit(VhileBlock block);
    protected abstract T visit(VhileLoop loop);
    protected abstract T visit(VhileEscape escape);


    public final T visit(VhileNode node) {
        return node.accept(this);
    }
}
