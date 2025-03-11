package week4.regex;

import week4.regex.ast.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RegexAnalyzer {


    public static boolean matchesEmptyWord(RegexNode regex) {
        RegexVisitor<Boolean> emptyWordVisitor = new RegexVisitor<>() {
            @Override
            protected Boolean visit(Letter letter) {
                return false;
            }

            @Override
            protected Boolean visit(Epsilon epsilon) {
                return true;
            }

            @Override
            protected Boolean visit(Repetition repetition) {
                return true;
            }

            @Override
            protected Boolean visit(Concatenation concatenation) {
                return visit(concatenation.getLeft()) && visit(concatenation.getRight());
            }

            @Override
            protected Boolean visit(Alternation alternation) {
                return visit(alternation.getLeft()) || visit(alternation.getRight());
            }
        };
        return emptyWordVisitor.visit(regex);
    }


    public static Set<Character> getFirst(String regex) {
        RegexNode node = RegexParser.parse(regex);
        return new RegexVisitor<ResultType>() {
            @Override
            protected ResultType visit(Alternation alternation) {
                ResultType leftResult = visit(alternation.getLeft());
                ResultType rightResult = visit(alternation.getRight());
                Set<Character> chars = new HashSet<>(leftResult.firstSet);
                chars.addAll(rightResult.firstSet);
                return new ResultType(chars, leftResult.matchesEmpty || rightResult.matchesEmpty);
            }

            @Override
            protected ResultType visit(Concatenation concatenation) {
                ResultType leftResult = visit(concatenation.getLeft());
                ResultType rightResult = visit(concatenation.getRight());
                Set<Character> chars = new HashSet<>(leftResult.firstSet);
                // Siin on see oluline koht, kus kontrollime tühjust:
                if (leftResult.matchesEmpty) chars.addAll(rightResult.firstSet);
                return new ResultType(chars, leftResult.matchesEmpty && rightResult.matchesEmpty);
            }

            @Override
            protected ResultType visit(Epsilon epsilon) {
                return new ResultType(Collections.emptySet(), true);
            }

            @Override
            protected ResultType visit(Letter letter) {
                return new ResultType(Collections.singleton(letter.getSymbol()), false);
            }

            @Override
            protected ResultType visit(Repetition repetition) {
                ResultType childResult = visit(repetition.getChild());
                return new ResultType(childResult.firstSet, true);
            }
        }.visit(node).firstSet;
    }

    private static class ResultType {
        private final Set<Character> firstSet;
        private final boolean matchesEmpty;

        public ResultType(Set<Character> firstSet, boolean matchesEmpty) {
            this.firstSet = firstSet;
            this.matchesEmpty = matchesEmpty;
        }
    }


    public static Set<String> getAllWords(RegexNode regex) {
        RegexVisitor<Set<String>> allWordsVisitor = new RegexVisitor<>() {
            @Override
            protected Set<String> visit(Alternation alternation) {
                Set<String> ret = new HashSet<>(visit(alternation.getLeft()));
                ret.addAll(visit(alternation.getRight()));
                return ret;
            }

            @Override
            protected Set<String> visit(Concatenation concatenation) {
                return combine(visit(concatenation.getLeft()), visit(concatenation.getRight()));
            }

            @Override
            protected Set<String> visit(Epsilon epsilon) {
                return Collections.singleton("");
            }

            @Override
            protected Set<String> visit(Letter letter) {
                return Collections.singleton(Character.toString(letter.getSymbol()));
            }

            @Override
            protected Set<String> visit(Repetition repetition) {
                Set<String> childWords = visit(repetition.getChild());
                if (childWords.equals(Collections.singleton("")))
                    return childWords;
                else
                    throw new RuntimeException("Infinite language");
            }
        };
        return allWordsVisitor.visit(regex);
    }

    private static Set<String> combine(Set<String> s1, Set<String> s2) {
        Set<String> result = new HashSet<>();
        for (String w1 : s1) {
            for (String w2 : s2) {
                result.add(w1 + w2);
            }
        }
        return result;
    }
}
