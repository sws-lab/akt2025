package week4.baselangs.bool;

import week4.baselangs.bool.ast.*;

import java.util.Set;
import java.util.TreeSet;

public class BoolMaster {

    // Meil on antud järgnev klass andmete kogumiseks:
    public static final class Stats {

        private final Set<Character> variables;
        private boolean found;

        public Stats() {
            variables = new TreeSet<>();
            found = false;
        }

        public Set<Character> getVariables() {
            return variables;
        }

        public boolean containsImp() {
            return found;
        }

        public void addVar(BoolVar node) {
            variables.add(node.getName());
        }

        public void foundImp() {
            found = true;
        }
    }

    // Analüüsida avaldist kasutades Stats klassi meetodid.
    public static Stats analyze(BoolNode exp) {
        Stats stats = new Stats();

        new BoolVisitor<Void>(){
            @Override
            public Void visit(BoolVar node) {
                stats.addVar(node);
                return null;
            }

            @Override
            public Void visit(BoolImp node) {
                stats.foundImp();
                visit(node.getAntedecent());
                visit(node.getConsequent());
                return null;
            }

            @Override
            protected Void visit(BoolOr node) {
                visit(node.getLeft());
                visit(node.getRight());
                return null;
            }

            @Override
            protected Void visit(BoolNot node) {
                visit(node.getExp());
                return null;
            }
        }.visit(exp);

        return stats;
    }


    // Teisendada avaldises implikatsiooni teiste operaatoritega.
    public static BoolNode transform(BoolNode exp) {
        return new BoolVisitor<BoolNode>() {
            @Override
            protected BoolNode visit(BoolImp node) {
                return new BoolOr(
                        new BoolNot(visit(node.getAntedecent())),
                        visit(node.getConsequent()));
            }

            @Override
            protected BoolNode visit(BoolOr node) {
                return new BoolOr(visit(node.getLeft()), visit(node.getRight()));
            }

            @Override
            protected BoolNode visit(BoolNot node) {
                return new BoolNot(visit(node.getExp()));
            }

            @Override
            protected BoolNode visit(BoolVar node) {
                return node;
            }
        }.visit(exp);
    }

    // Teha avaldisest otsustuspuu.
    public static DecisionTree createDecisionTree(BoolNode exp) {
        return new BoolVisitor<DecisionTree>() {
            @Override
            protected DecisionTree visit(BoolImp node) {
                DecisionTree cons = visit(node.getConsequent());
                return visit(node.getAntedecent()).compose(cons, DecisionTree.TRUE);
            }

            @Override
            protected DecisionTree visit(BoolOr node) {
                DecisionTree right = visit(node.getRight());
                return visit(node.getLeft()).compose(DecisionTree.TRUE, right);
            }

            @Override
            protected DecisionTree visit(BoolNot node) {
                return visit(node.getExp()).compose(DecisionTree.FALSE, DecisionTree.TRUE);
            }

            @Override
            protected DecisionTree visit(BoolVar node) {
                return DecisionTree.decision(node.getName(), DecisionTree.TRUE, DecisionTree.FALSE);
            }
        }.visit(exp);
    }

    /**
     * Otsustuspuu on tõeväärtusavaldise puukujuline esitus, kus vahetipudeks on ainult muutujad.
     * See esitab see avaldise tõeväärtustabelit, vt. meetod eval!
     *
     * Puu loomiseks võib kasutada meetod compose, mis jätkab otsustuspuud
     *    x.compose(y,z) tulemuseks on selline puu, et
     *       kui x on tõene, siis on tulemuseks y.
     *       vastasel korral on tulemuseks z.
     *    Seega on näiteks avaldise x eitus lihtsalt x.compose(FALSE, TRUE).
     *
     */
    public abstract static class DecisionTree {

        public static final DecisionTree TRUE = new TrueNode();
        public static final DecisionTree FALSE = new FalseNode();

        public static DecisionTree decision(char c, DecisionTree tc, DecisionTree fc) {
            return new DecisionNode(c, tc, fc);
        }

        public abstract boolean eval(Set<Character> tv);
        public abstract DecisionTree compose(DecisionTree trueDecision, DecisionTree falseDecision);


        private static class DecisionNode extends DecisionTree {
            private final Character variable;
            private final DecisionTree trueTree;
            private final DecisionTree falseTree;

            public DecisionNode(Character variable, DecisionTree trueTree, DecisionTree falseTree) {
                this.variable = variable;
                this.trueTree = trueTree;
                this.falseTree = falseTree;
            }

            @Override
            public boolean eval(Set<Character> tv) {
                return tv.contains(variable) ? trueTree.eval(tv) : falseTree.eval(tv);
            }

            @Override
            public DecisionTree compose(DecisionTree trueDecision, DecisionTree falseDecision) {
                return new DecisionNode(variable,
                        trueTree.compose(trueDecision, falseDecision),
                        falseTree.compose(trueDecision, falseDecision));
            }

            @Override
            public String toString() {
                return "(" + variable + " ? " + trueTree + " : " + falseTree + ")";
            }
        }

        private static class TrueNode extends DecisionTree {
            @Override
            public boolean eval(Set<Character> tv) {
                return true;
            }

            @Override
            public DecisionTree compose(DecisionTree trueDecision, DecisionTree falseDecision) {
                return trueDecision;
            }

            @Override
            public String toString() {
                return "true";
            }
        }

        private static class FalseNode extends DecisionTree {
            @Override
            public boolean eval(Set<Character> tv) {
                return false;
            }

            @Override
            public DecisionTree compose(DecisionTree trueDecision, DecisionTree falseDecision) {
                return falseDecision;
            }

            @Override
            public String toString() {
                return "false";
            }
        }

    }
}
