package week5.grammar;

import week4.regex.RegexParser;
import week4.regex.ast.*;

public class RegexGrammar {

    // Konstandid ilutrüki jaoks
    private static final char EPS = 'ε';
    private static final String ARROW = " → ";

    public static void printGrammar(RegexNode node) {
        RegexVisitor<Void> visitor = new RegexVisitor<>() {

            private char nextNt = 'A';
            // Genereeri järgmine mitte-terminal (eeldades, et neid liiga palju ei ole).
            private char getNextNt() {
                if (nextNt == 'S') nextNt++; // Jätame S vahele, et seda saaks kasutada algsümbolina.
                return nextNt++;
            }

            @Override
            protected Void visit(Letter letter) {
                return null;
            }

            @Override
            protected Void visit(Epsilon epsilon) {
                return null;
            }

            @Override
            protected Void visit(Repetition repetition) {
                return null;
            }

            @Override
            protected Void visit(Concatenation concatenation) {
                return null;
            }

            @Override
            protected Void visit(Alternation alternation) {
                return null;
            }
        };

        visitor.visit(node);
    }

    public static void main(String[] args) {
        printGrammar(RegexParser.parse("(a|bc)*"));
    }
}
