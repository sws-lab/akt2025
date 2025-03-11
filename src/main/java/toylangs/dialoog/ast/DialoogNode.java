package toylangs.dialoog.ast;

import toylangs.AbstractNode;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static toylangs.dialoog.ast.DialoogBinary.BinOp.*;
import static toylangs.dialoog.ast.DialoogUnary.UnOp.DialoogNeg;
import static toylangs.dialoog.ast.DialoogUnary.UnOp.DialoogNot;

public abstract class DialoogNode extends AbstractNode {

    // Programm ja muutujate deklaratsioonid
    public static DialoogNode prog(List<DialoogDecl> decls, DialoogNode expr) { return new DialoogProg(decls, expr); }
    public static List<DialoogDecl> decls(DialoogDecl... ds) { return Arrays.asList(ds); }
    public static DialoogDecl iv(String name) { return new DialoogDecl(name, true); }
    public static DialoogDecl bv(String name) { return new DialoogDecl(name, false); }

    // Literaalid ja muutuja kasutus
    public static DialoogNode il(int value) { return new DialoogLitInt(value); }
    public static DialoogNode bl(boolean value) { return new DialoogLitBool(value); }
    public static DialoogNode var(String name) { return new DialoogVar(name); }

    // Tingimus
    public static DialoogNode ifte(DialoogNode guard, DialoogNode trueExpr, DialoogNode falseExpr) {
        return new DialoogTernary(guard, trueExpr, falseExpr);
    }

    // Operatsioonid sümboli järgi
    public static DialoogNode binop(String op, DialoogNode left, DialoogNode right) {
        return new DialoogBinary(op, left, right);
    }
    public static DialoogNode unop(String op, DialoogNode expr) {
        return new DialoogUnary(op, expr);
    }

    // Võrdlus
    public static DialoogNode eq(DialoogNode left, DialoogNode right) { return new DialoogBinary(DialoogEq, left, right); }

    // Loogika
    public static DialoogNode not(DialoogNode expr) { return new DialoogUnary(DialoogNot, expr); }
    public static DialoogNode and(DialoogNode left, DialoogNode right) { return new DialoogBinary(DialoogAnd, left, right); }
    public static DialoogNode or(DialoogNode left, DialoogNode right) { return new DialoogBinary(DialoogOr, left, right); }

    // Aritmeetika
    public static DialoogNode neg(DialoogNode expr) { return new DialoogUnary(DialoogNeg, expr); }
    public static DialoogNode add(DialoogNode left, DialoogNode right) { return new DialoogBinary(DialoogAdd, left, right); }
    public static DialoogNode sub(DialoogNode left, DialoogNode right) { return new DialoogBinary(DialoogSub, left, right); }
    public static DialoogNode mul(DialoogNode left, DialoogNode right) { return new DialoogBinary(DialoogMul, left, right); }
    public static DialoogNode div(DialoogNode left, DialoogNode right) { return new DialoogBinary(DialoogDiv, left, right); }

    public abstract <T> T accept(DialoogAstVisitor<T> visitor);

    public static void main(String[] args) throws IOException {
        DialoogNode test = prog(
                decls(iv("x"), bv("y")),
                add(var("x"), var("y"))
        );
        test.renderPngFile(Paths.get("graphs", "dialoog.png"));
        System.out.println(test);
    }
}
