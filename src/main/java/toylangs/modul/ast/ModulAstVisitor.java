package toylangs.modul.ast;

public abstract class ModulAstVisitor<T> {

    protected abstract T visit(ModulNum num);
    protected abstract T visit(ModulVar var);
    protected abstract T visit(ModulNeg neg);
    protected abstract T visit(ModulAdd add);
    protected abstract T visit(ModulMul mul);
    protected abstract T visit(ModulPow pow);
    protected abstract T visit(ModulProg prog);

    public final T visit(ModulNode node) {
        return node.accept(this);
    }
}
