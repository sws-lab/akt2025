package toylangs.bolog.ast;

public abstract class BologAstVisitor<T> {

    protected abstract T visit(BologLit tv);
    protected abstract T visit(BologVar var);
    protected abstract T visit(BologNand nand);
    protected abstract T visit(BologImp imp);

    public final T visit(BologNode node) {
        return node.accept(this);
    }

}
