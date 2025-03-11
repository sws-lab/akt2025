package toylangs.parm.ast;

public abstract class ParmAstVisitor<T> {

    protected abstract T visit(ParmLit lit);
    protected abstract T visit(ParmVar var);
    protected abstract T visit(ParmPlus plus);
    protected abstract T visit(ParmUpdate up);
    protected abstract T visit(ParmCompose comp);

    public final T visit(ParmNode node) {
        return node.accept(this);
    }

}
