package toylangs.dialoog.ast;

public class DialoogAstVisitor<T> {

    protected T visit(DialoogLitInt num) {
        return null;
    }

    protected T visit(DialoogLitBool tv) {
        return null;
    }

    protected T visit(DialoogVar var) {
        return null;
    }

    protected T visit(DialoogUnary unary)  {
        return visit(unary.getExpr());
    }

    protected T visit(DialoogBinary binary)  {
        visit(binary.getLeftExpr());
        return visit(binary.getRightExpr());
    }

    protected T visit(DialoogTernary ifte) {
        visit(ifte.getGuardExpr());
        visit(ifte.getTrueExpr());
        return visit(ifte.getFalseExpr());
    }

    protected T visit(DialoogDecl decl) {
        return null;
    }

    protected T visit(DialoogProg prog) {
        for (DialoogDecl decl : prog.getDecls()) visit(decl);
        return visit(prog.getExpression());
    }

    public final T visit(DialoogNode node) {
        return node.accept(this);
    }


}
