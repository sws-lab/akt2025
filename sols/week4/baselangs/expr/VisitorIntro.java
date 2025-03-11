package week4.baselangs.expr;

import week4.baselangs.expr.ast.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class VisitorIntro {

    // Mõnikord me ei taha muuta AST klasse:
    // Siin on ainult üks rida puudu...
    public static int eval(ExprNode expr) {
        // Proovi siin ka IntelliJ soovitust see switch avaldiseks teisendada, mis on võimalik alates Java 21-st.
        if (expr instanceof ExprNum num) {
            return num.getValue();
        } else if (expr instanceof ExprAdd add) {
            return eval(add.getLeft()) + eval(add.getRight());
        } else if (expr instanceof ExprDiv div) {
            return eval(div.getNumerator()) / eval(div.getDenominator());
        } else if (expr instanceof ExprNeg neg) {
            return -eval(neg.getExpr());
        } else {
            // Siia ei peaks kunagi jõudma (v.a. null).
            throw new IllegalArgumentException();
        }
    }

    // Proovime nüüd selle sama asja visitor abil teha.
    public static int evalWithVisitor(ExprNode expr) {
        // Kirjutame siia "null" asemel "new ExprVisitor<Integer>()" ja siis IDE abil genereeri meetodid.
        ExprVisitor<Integer> visitor = new ExprVisitor<>() {
            @Override
            public Integer visit(ExprNum num) {
                return num.getValue();
            }

            @Override
            public Integer visit(ExprNeg neg) {
                return - visit(neg.getExpr());
            }

            @Override
            public Integer visit(ExprAdd add) {
                return visit(add.getLeft()) + visit(add.getRight());
            }

            @Override
            public Integer visit(ExprDiv div) {
                return visit(div.getNumerator()) / visit(div.getDenominator());
            }
        };
        return expr.accept(visitor);
    }

    // koguda kõik tipus esinevad arvud.
    public static Set<Integer> getAllValues(ExprNode expr) {
        ExprVisitor<Set<Integer>> visitor = new ExprVisitor<>() {
            @Override
            protected Set<Integer> visit(ExprNum num) {
                return Collections.singleton(num.getValue());
            }

            @Override
            protected Set<Integer> visit(ExprNeg neg) {
                return visit(neg.getExpr());
            }

            @Override
            protected Set<Integer> visit(ExprDiv div) {
                Set<Integer> result = new HashSet<>();
                result.addAll(visit(div.getDenominator()));
                result.addAll(visit(div.getNumerator()));
                return result;
            }

            @Override
            protected Set<Integer> visit(ExprAdd add) {
                Set<Integer> result = new HashSet<>();
                result.addAll(visit(add.getLeft()));
                result.addAll(visit(add.getRight()));
                return result;
            }
        };
        return expr.accept(visitor);
    }


    // Proovi nüüd sama kood ilma kordusteta ümber kirjutada kasutades BaseVisitorit.
    public static Set<Integer> getAllValuesBaseVisitor(ExprNode expr) {

        ExprVisitor<Set<Integer>> visitor = new ExprVisitor.BaseVisitor<>() {
            @Override
            protected Set<Integer> visit(ExprNum num) {
                return Collections.singleton(num.getValue());
            }

            @Override
            protected Set<Integer> aggregateResult(Set<Integer> aggregate, Set<Integer> nextResult) {
                Set<Integer> result = new HashSet<>();
                result.addAll(aggregate);
                result.addAll(nextResult);
                return result;
            }
        };

        return expr.accept(visitor);
    }


    // Võime muidugi ka ainult tipudes tegutseda ja elemente lihtsalt hulka kokku koguda.
    // NB! Kasutame siin siseklasse ja kui tahta näiteks loendada tipude arvu, siis Java kurdab,
    // et väline muutuja ei ole "final". Võid proovida "set" asendada reaga "int count = 0" ja elemendi
    // lisamise asemel teha "count++". IntelliJ oskab seda ise asendada ühe-elemendilise jadaga.
    public static Set<Integer> getAllValuesWithBaseVisitorImperative(ExprNode expr) {

        Set<Integer> set = new HashSet<>();

        ExprVisitor<Void> visitor = new ExprVisitor.BaseVisitor<>() {
            @Override
            protected Void visit(ExprNum num) {
                set.add(num.getValue());
                return null; // <-- Geneerikute Void tüüp nõuab tagastust :(
            }
        };

        expr.accept(visitor);
        return set;
    }

}
