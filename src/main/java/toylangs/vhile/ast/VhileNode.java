package toylangs.vhile.ast;

import toylangs.AbstractNode;

import java.util.List;

public abstract class VhileNode extends AbstractNode {

    public static VhileNum num(int value) {
        return new VhileNum(value);
    }

    public static VhileVar var(String name) {
        return new VhileVar(name);
    }

    public static VhileBinOp add(VhileExpr left, VhileExpr right) {
        return new VhileBinOp(VhileBinOp.Op.Add, left, right);
    }

    public static VhileBinOp mul(VhileExpr left, VhileExpr right) {
        return new VhileBinOp(VhileBinOp.Op.Mul, left, right);
    }

    public static VhileBinOp eq(VhileExpr left, VhileExpr right) {
        return new VhileBinOp(VhileBinOp.Op.Eq, left, right);
    }

    public static VhileBinOp neq(VhileExpr left, VhileExpr right) {
        return new VhileBinOp(VhileBinOp.Op.Neq, left, right);
    }

    public static VhileAssign assign(String name, VhileExpr expr) {
        return new VhileAssign(name, expr);
    }

    public static VhileBlock block(List<VhileStmt> stmts) {
        return new VhileBlock(stmts);
    }

    public static VhileBlock block(VhileStmt... stmts) {
        return block(List.of(stmts));
    }

    public static VhileLoop loop(VhileExpr condition, VhileStmt body) {
        return new VhileLoop(condition, body);
    }

    public static VhileEscape escape(int level) {
        return new VhileEscape(level);
    }


    public abstract <T> T accept(VhileAstVisitor<T> visitor);
}
