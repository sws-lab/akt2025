package toylangs.hulk.ast;

import toylangs.AbstractNode;
import toylangs.hulk.ast.expressions.HulkBinOp;
import toylangs.hulk.ast.expressions.HulkExpr;
import toylangs.hulk.ast.expressions.HulkLit;
import toylangs.hulk.ast.expressions.HulkVar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// See on ASTi tippude abstraktne ülemklass.
// Siin olevate staatiliste meetodite abil saad ehitada ASTi.
public abstract class HulkNode extends AbstractNode {

    public static HulkProg prog(List<HulkStmt> stmts) {
        return new HulkProg(stmts);
    }

    public static HulkProg prog(HulkStmt... stmts) {
        return prog(Arrays.asList(stmts));
    }

    public static HulkStmt stmt(Character name, HulkExpr expr, HulkCond cond) {
        return new HulkStmt(name, expr, cond);
    }

    public static HulkCond cond(HulkExpr subset, HulkExpr superset) {
        return new HulkCond(subset, superset);
    }

    public static HulkExpr var(Character name) {
        return new HulkVar(name);
    }

    public static HulkExpr lit(Character... elements) {
        return new HulkLit(new HashSet<>(Arrays.asList(elements)));
    }

    public static HulkExpr lit(Set<Character> elements) {
        return new HulkLit(elements);
    }

    public static HulkExpr binop(Character op, HulkExpr left, HulkExpr right) {
        return new HulkBinOp(op, left, right);
    }

    public abstract String prettyPrint();

    public abstract <T> T accept(HulkAstVisitor<T> visitor);
}
