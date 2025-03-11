package toylangs.hulk;

import toylangs.hulk.ast.*;
import toylangs.hulk.ast.expressions.*;

import java.util.*;

import static toylangs.hulk.ast.HulkNode.*;

public class HulkEvaluator {

    public static void eval(HulkNode node, Map<Character, Set<Character>> env) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        ArrayList<HulkStmt> laused = new ArrayList<>();
        laused.add(stmt('A', lit('a'), null));  // A := {a}
        laused.add(stmt('B', lit('b'), null));  // B := {b}
        laused.add(stmt('D', binop('+', var('A'), var('B')), null));  // D := (A+B)
        HulkProg s = prog(laused);
        System.out.println(s);
        System.out.println();

        Map<Character, Set<Character>> env = new HashMap<>();
        eval(s, env);
        System.out.println(env);  // {A=[a], B=[b], D=[a, b]}
    }
}
