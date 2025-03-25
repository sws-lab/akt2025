package week5.grammar;

import week4.regex.RegexParser;
import week4.regex.ast.*;

public class RegexGrammar {

    // Konstandid ilutrüki jaoks
    private static final char EPS = 'ε';
    private static final String ARROW = " → ";

    public static void printGrammar(RegexNode node) {
        RegexVisitor<Void> visitor = new RegexVisitor<>() {
            private char nt = 'S'; // Praegu defineeritav mitte-terminal

            private char nextNt = 'A';
            // Genereeri järgmine mitte-terminal (eeldades, et neid liiga palju ei ole).
            private char getNextNt() {
                if (nextNt == 'S') nextNt++; // Jätame S vahele, et seda saaks kasutada algsümbolina.
                return nextNt++;
            }

            @Override
            protected Void visit(Letter letter) {
                System.out.println(nt + ARROW + letter.getSymbol());
                return null;
            }

            @Override
            protected Void visit(Epsilon epsilon) {
                System.out.println(nt + ARROW + EPS);
                return null;
            }

            @Override
            protected Void visit(Repetition repetition) {
                char childNt = getNextNt();
                System.out.println(nt + ARROW + childNt + nt);
                System.out.println(nt + ARROW + EPS);
                nt = childNt;
                visit(repetition.getChild());
                return null;
            }

            @Override
            protected Void visit(Concatenation concatenation) {
                char leftNt = getNextNt();
                char rightNt = getNextNt();
                System.out.println(nt + ARROW + leftNt + rightNt);
                nt = leftNt;
                visit(concatenation.getLeft());
                nt = rightNt;
                visit(concatenation.getRight());
                return null;
            }

            @Override
            protected Void visit(Alternation alternation) {
                char leftNt = getNextNt();
                char rightNt = getNextNt();
                System.out.println(nt + ARROW + leftNt);
                System.out.println(nt + ARROW + rightNt);
                nt = leftNt;
                visit(alternation.getLeft());
                nt = rightNt;
                visit(alternation.getRight());
                return null;
            }
        };

        visitor.visit(node);
    }

    public static void main(String[] args) {
        printGrammar(RegexParser.parse("(a|bc)*"));
    }
}
