package eksam2.ast;

import toylangs.AbstractNode;

import java.util.List;

public abstract class TraksNode extends AbstractNode {

    public static TraksNum num(int value) {
        return new TraksNum(value);
    }

    public static TraksVar var(String name) {
        return new TraksVar(name);
    }

    public static TraksBinOp add(TraksExpr left, TraksExpr right) {
        return new TraksBinOp(TraksBinOp.Op.Add, left, right);
    }

    public static TraksBinOp sub(TraksExpr left, TraksExpr right) {
        return new TraksBinOp(TraksBinOp.Op.Sub, left, right);
    }

    public static TraksBinOp geq(TraksExpr left, TraksExpr right) {
        return new TraksBinOp(TraksBinOp.Op.Geq, left, right);
    }

    public static TraksAssign assign(String name, TraksExpr expr) {
        return new TraksAssign(name, expr);
    }

    public static TraksBlock block(List<TraksStmt> stmts) {
        return new TraksBlock(stmts);
    }

    public static TraksBlock block(TraksStmt... stmts) {
        return block(List.of(stmts));
    }

    public static TraksCheck check(TraksExpr expr) {
        return new TraksCheck(expr);
    }

    public static TraksAction action(TraksStmt stmt) {
        return new TraksAction(stmt);
    }


    public abstract <T> T accept(TraksAstVisitor<T> visitor);
}
