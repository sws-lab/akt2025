package eksam3.ast;

public abstract class SimpAstVisitor<T> {

    protected abstract T visit(SimpNum num);
    protected abstract T visit(SimpAdd add);
    protected abstract T visit(SimpMul mul);
    protected abstract T visit(SimpMem mem);
    protected abstract T visit(SimpProg prog);

    public T visit(SimpNode node) {
        return node.accept(this);
    }
}
