package toylangs.dialoog;

import toylangs.dialoog.ast.*;

import static toylangs.dialoog.ast.DialoogNode.*;
import static toylangs.dialoog.DialoogMaster.Type.TBool;
import static toylangs.dialoog.DialoogMaster.Type.TInt;

public class DialoogMaster {

    public enum Type { TInt, TBool }

    public static Type typecheck(DialoogNode prog) {
        throw new UnsupportedOperationException();
    }

    public static DialoogNode symbex(DialoogNode prog) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        DialoogNode expr = ifte(eq(var("x"), il(10)), var("error"), var("good"));
        System.out.println(symbex(expr));
    }
}
