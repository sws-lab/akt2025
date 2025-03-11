package toylangs.hulk;

import toylangs.hulk.ast.*;
import toylangs.hulk.ast.expressions.HulkBinOp;
import toylangs.hulk.ast.expressions.HulkLit;
import toylangs.hulk.ast.expressions.HulkVar;

import java.util.*;

import static toylangs.hulk.ast.HulkNode.*;

public class HulkEvaluator {

    public static void eval(HulkNode node, Map<Character, Set<Character>> env) {
        HulkAstVisitor<Set<Character>> visitor = new HulkAstVisitor<>() {
            @Override
            public Set<Character> visit(HulkLit lit) {
                return lit.getElements();
            }

            @Override
            public Set<Character> visit(HulkVar variable) {
                Character name = variable.getName();
                if (env.containsKey(name))
                    return env.get(variable.getName());
                else
                    throw new NoSuchElementException(name.toString());

            }

            @Override
            public Set<Character> visit(HulkBinOp binop) {
                Set<Character> left = visit(binop.getLeft());
                Set<Character> right = visit(binop.getRight());
                Set<Character> result = new HashSet<>(left);
                switch (binop.getOp()) {
                    case '+' -> result.addAll(right);
                    case '-' -> result.removeAll(right);
                    case '&' -> result.retainAll(right);
                }
                return result;
            }

            @Override
            public Set<Character> visit(HulkStmt stmt) {
                if (stmt.getCond() == null || visitCond(stmt.getCond())) {
                    Character name = stmt.getName();
                    Set<Character> elements = visit(stmt.getExpr());
                    env.put(name, elements);
                }
                return null;
            }

            @Override
            public Set<Character> visit(HulkProg program) {
                for (HulkStmt stmt : program.getStatements()) visit(stmt);
                return null;
            }

            @Override
            public Set<Character> visit(HulkCond cond) {
                throw new UnsupportedOperationException("Use boolean visitCond instead!");
            }

            private boolean visitCond(HulkCond cond) {
                Set<Character> subset = visit(cond.getSubset());
                Set<Character> superset = visit(cond.getSuperset());
                return superset.containsAll(subset);
            }
        };

        visitor.visit(node);
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
