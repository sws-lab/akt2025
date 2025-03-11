package week4.baselangs.expr.ast;

public abstract class ExprNode {


    // Võimaldavad natuke mugavamalt luua neid objekte:
    public static ExprNode num(int value) {
        return new ExprNum(value);
    }

    public static ExprNode neg(ExprNode expr) {
        return new ExprNeg(expr);
    }

    public static ExprNode add(ExprNode left, ExprNode right) {
        return new ExprAdd(left, right);
    }

    public static ExprNode div(ExprNode numerator, ExprNode denominator) {
        return new ExprDiv(numerator, denominator);
    }

    // Visitor ja listener implementatsiooniks:
    public abstract <T> T accept(ExprVisitor<T> visitor);

    // Avaldise väärtustamiseks:
    public abstract int eval();

    public static void main(String[] args) {
        ExprNode expr = div(add(num(5), add(num(3), neg(num(2)))), num(2));
        System.out.println(expr.eval()); // tulemus: 3
    }

}
