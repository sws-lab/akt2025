package week4.regex;

import week4.regex.ast.*;

public class RegexPrettyPrinter {

    // Ülesanne on ilusasti (see tähendab võimalikult väheste sulgudega) väljastada regulaaravaldised.
    // Vaata aritmeetiliste avaldiste (expr) ilutrükki klassis ExprMaster.
    public static String pretty(RegexNode regex) {
        RegexVisitor<StringBuilder> prettyRegexVisitor = new RegexVisitor<>() {
            final StringBuilder sb = new StringBuilder();

            @Override
            protected StringBuilder visit(Letter letter) {
                return sb.append(letter);
            }

            @Override
            protected StringBuilder visit(Epsilon epsilon) {
                return sb.append(epsilon);
            }

            @Override
            protected StringBuilder visit(Alternation alt) {
                visit(alt.getLeft(), priorityOf(alt)).append('|');
                visit(alt.getRight(), priorityOf(alt));
                return sb;
            }

            @Override
            protected StringBuilder visit(Concatenation concat) {
                visit(concat.getLeft(), priorityOf(concat));
                visit(concat.getRight(), priorityOf(concat));
                return sb;
            }

            @Override
            protected StringBuilder visit(Repetition rep) {
                visit(rep.getChild(), priorityOf(rep)+1); // <-- (a*)* ümber lisasulud.
                return sb.append('*');
            }

            private StringBuilder visit(RegexNode node, int contextPrio) {
                boolean paren = priorityOf(node) < contextPrio;
                if (paren) sb.append('(');
                super.visit(node);
                if (paren) sb.append(')');
                return sb;
            }

        };
        return prettyRegexVisitor.visit(regex).toString();
    }

    private static int priorityOf(RegexNode regex) {
        if (regex instanceof Alternation) return 1;
        if (regex instanceof Concatenation) return 2;
        if (regex instanceof Repetition) return 3;
        return 4;
    }
}
