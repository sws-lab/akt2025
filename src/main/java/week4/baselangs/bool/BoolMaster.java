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
        return null;
    }


    // Teisendada avaldises implikatsiooni teiste operaatoritega.
    public static BoolNode transform(BoolNode exp) {
        return null;
    }

    // Teha avaldisest otsustuspuu.
    public static DecisionTree createDecisionTree(BoolNode exp) {
        return null;
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
