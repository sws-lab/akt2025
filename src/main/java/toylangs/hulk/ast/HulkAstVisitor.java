package toylangs.hulk.ast;

import toylangs.hulk.ast.expressions.HulkBinOp;
import toylangs.hulk.ast.expressions.HulkLit;
import toylangs.hulk.ast.expressions.HulkVar;

public abstract class HulkAstVisitor<T> {

    public abstract T visit(HulkLit lit);
    public abstract T visit(HulkVar variable);
    public abstract T visit(HulkBinOp binop);
    public abstract T visit(HulkStmt stmt);
    public abstract T visit(HulkProg programm);
    public abstract T visit(HulkCond cond);

    public T visit(HulkNode node) {
        return node.accept(this);
    }
}
