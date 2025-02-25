package week3;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FiniteAutomatonTest {
    public static String lastTestDescription = "";

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(1000, TimeUnit.MILLISECONDS));

    @Test
    public void dfaIlmaEpsilonita1() {
        FiniteAutomaton fa = automaton(
                0,
                Collections.singletonList(1),
                tr(0, 'b', 0),
                tr(2, 'a', 1),
                tr(0, 'c', 2),
                tr(1, 'd', 0)
        );

        checkAutomaton(fa, "bca", true);
        checkAutomaton(fa, "bbbca", true);
        checkAutomaton(fa, "ca", true);
        checkAutomaton(fa, "cadbbbca", true);
        checkAutomaton(fa, "", false);
        checkAutomaton(fa, "b", false);
        checkAutomaton(fa, "a", false);
        checkAutomaton(fa, "c", false);
        checkAutomaton(fa, "cad", false);
        checkAutomaton(fa, "abca", false);
    }

    @Test
    public void dfaIlmaEpsilonita2() {
        // (a|b)*(aba|bab)(a|b)*
        FiniteAutomaton fa = new FiniteAutomaton();
        fa.addState(12);
        fa.addState(2);
        fa.addState(4);
        fa.addState(1);
        fa.addState(0);
        fa.addState(3);
        fa.setStartState(0);
        fa.addAcceptingState(12);
        fa.addTransition(12, 'b', 12);
        fa.addTransition(12, 'a', 12);
        fa.addTransition(2, 'b', 3);
        fa.addTransition(2, 'a', 2);
        fa.addTransition(4, 'b', 12);
        fa.addTransition(4, 'a', 2);
        fa.addTransition(1, 'b', 1);
        fa.addTransition(1, 'a', 4);
        fa.addTransition(0, 'b', 1);
        fa.addTransition(0, 'a', 2);
        fa.addTransition(3, 'b', 1);
        fa.addTransition(3, 'a', 12);
        checkAutomaton(fa, "", false);
        checkAutomaton(fa, "aba", true);
        checkAutomaton(fa, "abb", false);
        checkAutomaton(fa, "babbb", true);
        checkAutomaton(fa, "baaaabaaabbaaaabab", true);
        checkAutomaton(fa, "abaaabbaabbbaabaaaaabbaababbabababbaababbbbbbb", true);
        checkAutomaton(fa, "bbbaabab", true);
        checkAutomaton(fa, "bbbabbabaabbbabab", true);
        checkAutomaton(fa, "baabaab", true);
        checkAutomaton(fa, "abababaaaababbaaabb", true);
        checkAutomaton(fa, "babbabbabbaabbabbababbbaaabaaaabaa", true);
        checkAutomaton(fa, "babaababbaabbaaabbababaaaa", true);
        checkAutomaton(fa, "abbabbbbaaaabbaababbbabbbbaabbabbabbbaa", true);
        checkAutomaton(fa, "bbaabbbbbaaababbababbbbaaabbbaaabaabbba", true);
        checkAutomaton(fa, "b", false);
        checkAutomaton(fa, "babaabbbabbbbaabaabaaaabb", true);
        checkAutomaton(fa, "abbabbbbabbabaaaaaaaaa", true);
        checkAutomaton(fa, "aaaabaababbbbaaabbaabaabab", true);
        checkAutomaton(fa, "bbbaab", false);
        checkAutomaton(fa, "baabaaababbbbaaaaabbaabbabaabbabbbaabbaabbabbbbabababbbabbabbbbabaababbaaaaaaaaabbabaaabbaababaababaabbbbbba", true);
        checkAutomaton(fa, "baababa", true);
        checkAutomaton(fa, "aabbbaaaaabbbbababbabb", true);
        checkAutomaton(fa, "abaabbbbababaaaa", true);
        checkAutomaton(fa, "aaababaaabbbababababab", true);
        checkAutomaton(fa, "aaaababbbbbaaaaaabbbbbbaaaaababbababaaa", true);
        checkAutomaton(fa, "abbabbbaaabbb", true);
    }

    @Test
    public void dfaIlmaEpsilonita3() {
        // a(b|c)*b(a|c)*c(a|b)*
        FiniteAutomaton fa = new FiniteAutomaton();
        fa.addState(1);
        fa.addState(5);
        fa.addState(7);
        fa.addState(6);
        fa.addState(4);
        fa.addState(2);
        fa.addState(0);
        fa.setStartState(0);
        fa.addAcceptingState(7);
        fa.addAcceptingState(6);
        fa.addAcceptingState(4);
        fa.addTransition(1, 'b', 2);
        fa.addTransition(1, 'c', 1);
        fa.addTransition(5, 'c', 6);
        fa.addTransition(5, 'a', 5);
        fa.addTransition(7, 'b', 7);
        fa.addTransition(7, 'a', 7);
        fa.addTransition(6, 'b', 7);
        fa.addTransition(6, 'c', 6);
        fa.addTransition(6, 'a', 6);
        fa.addTransition(4, 'b', 4);
        fa.addTransition(4, 'c', 4);
        fa.addTransition(4, 'a', 6);
        fa.addTransition(2, 'b', 2);
        fa.addTransition(2, 'c', 4);
        fa.addTransition(2, 'a', 5);
        fa.addTransition(0, 'a', 1);
        checkAutomaton(fa, "", false);
        checkAutomaton(fa, "cb", false);
        checkAutomaton(fa, "cbbaaccbbcabaccabaccccaacabaabbbaacbabacccbbbabbcacbbaba", false);
        checkAutomaton(fa, "acbaaacbcbbbaaccbbcacaabac", false);
        checkAutomaton(fa, "abccbbbccb", true);
        checkAutomaton(fa, "b", false);
        checkAutomaton(fa, "ccbcba", false);
        checkAutomaton(fa, "bcbbccabaccbbcaaccaab", false);
        checkAutomaton(fa, "accbacbcbbcbaaaca", false);
        checkAutomaton(fa, "cbcccacaacbbabcbbacaabcbccbacbabb", false);
        checkAutomaton(fa, "baaaccbaaccccccbccabaaacbabcabccaabcccaaaaccacccabacababccbabcab", false);
        checkAutomaton(fa, "bcbaccaaaacccbabcccbababbbcbbcbabbbcbaabacbbaba", false);
        checkAutomaton(fa, "abccacccaccbbab", true);
        checkAutomaton(fa, "bcaacaacacbbcccc", false);
        checkAutomaton(fa, "caab", false);
        checkAutomaton(fa, "acabcbababacbacacaacbcccbbcbcc", false);
        checkAutomaton(fa, "aacbbcacaaaaaaccaaabcbcacc", false);
        checkAutomaton(fa, "cbababaaaabaaccbaaccaccaaccbcabcaabbbcaccbb", false);
        checkAutomaton(fa, "cccabcacacaaabaccabbcbaabcabac", false);
        checkAutomaton(fa, "abcabbcbbbabbbccaabcbaabccbaacabcccccccaacccccbccbabbbcbcaa", false);
        checkAutomaton(fa, "bbcababaacabacbaacbcbabaabcbcb", false);
        checkAutomaton(fa, "cbccacbcbbbccb", false);
        checkAutomaton(fa, "cbbaabbcabaacc", false);
        checkAutomaton(fa, "bbbccabacccacabaccbcccb", false);
        checkAutomaton(fa, "acbabbccbbcbacaacacccbaaacbbbbabcacba", false);
    }

    @Test
    public void nfaIlmaEpsilonita1() {
        FiniteAutomaton fa = automaton(
                0,
                Collections.singletonList(1),
                tr(0, 'a', 0),
                tr(0, 'b', 0),
                tr(0, 'a', 1)
        );

        checkAutomaton(fa, "", false);
        checkAutomaton(fa, "a", true);
        checkAutomaton(fa, "b", false);
        checkAutomaton(fa, "abba", true);
        checkAutomaton(fa, "babbbbb", false);
        checkAutomaton(fa, "babbbbba", true);
        checkAutomaton(fa, "bbbbb", false);
        checkAutomaton(fa, "ababa", true);
        checkAutomaton(fa, "c", false);
        checkAutomaton(fa, "abcba", false);
        checkAutomaton(fa, "u", false);
    }

    @Test
    public void nfaIlmaEpsilonita2() {
        FiniteAutomaton fa = automaton(
                0,
                Collections.singletonList(3),
                tr(0, 'a', 0),
                tr(0, 'b', 0),
                tr(0, 'a', 1),
                tr(1, 'b', 2),
                tr(2, 'a', 3),
                tr(3, 'a', 3),
                tr(3, 'b', 3)
        );

        checkAutomaton(fa, "", false);
        checkAutomaton(fa, "a", false);
        checkAutomaton(fa, "b", false);
        checkAutomaton(fa, "abba", false);
        checkAutomaton(fa, "bababbbb", true);
        checkAutomaton(fa, "bbbbb", false);
        checkAutomaton(fa, "ababa", true);
        checkAutomaton(fa, "aababa", true);
        checkAutomaton(fa, "c", false);
        checkAutomaton(fa, "abcbaba", false);
        checkAutomaton(fa, "u", false);
    }


    @Test
    public void nfaKoosEpsiloniga1() {
        FiniteAutomaton fa = automaton(
                0,
                Collections.singletonList(1),
                tr(0, 'a', 0),
                tr(0, 'b', 0),
                tr(0, 'c', 2),
                tr(1, 'd', 0),
                tr(0, null, 1),
                tr(0, null, 2),
                tr(2, null, 1)
        );

        checkAutomaton(fa, "", true);
        checkAutomaton(fa, "aabb", true);
        checkAutomaton(fa, "dd", true);
        checkAutomaton(fa, "bad", true);
        checkAutomaton(fa, "dad", true);
        checkAutomaton(fa, "acdc", true);
        checkAutomaton(fa, "abc", true);
        checkAutomaton(fa, "abcc", false);
        checkAutomaton(fa, "abca", false);
    }

    @Test
    public void nfaKoosEpsiloniga2() {
        // (a|b)*b(a|b)
        FiniteAutomaton fa = new FiniteAutomaton();
        fa.addState(0);
        fa.addState(2);
        fa.addState(5);
        fa.addState(4);
        fa.addState(1);
        fa.addState(3);
        fa.setStartState(0);
        fa.addAcceptingState(1);
        fa.addTransition(0, null, 2);
        fa.addTransition(0, null, 3);
        fa.addTransition(2, 'b', 5);
        fa.addTransition(5, 'b', 1);
        fa.addTransition(5, 'a', 1);
        fa.addTransition(4, null, 2);
        fa.addTransition(4, null, 3);
        fa.addTransition(3, 'b', 4);
        fa.addTransition(3, 'a', 4);
        checkAutomaton(fa, "", false);
        checkAutomaton(fa, "abb", true);
        checkAutomaton(fa, "aabbbaabbbbbbabbaababbabaaaabbaaaaabaabbaababbabbabaabbbbaabbabbaaaaba", true);
        checkAutomaton(fa, "abbbabbbabbbbbbaaaabbbaaabaabbaaaababbaabaaabbbbbababaaaaabbbabab", false);
        checkAutomaton(fa, "bbaaaa", false);
        checkAutomaton(fa, "babbb", true);
        checkAutomaton(fa, "babaabbbbbaaabbaabbba", true);
        checkAutomaton(fa, "aabbba", true);
        checkAutomaton(fa, "bbbbbbbabbbbabbbbbababbaabb", true);
        checkAutomaton(fa, "aabbbbbbbabbabaaa", false);
        checkAutomaton(fa, "bbabbaaaabbb", true);
        checkAutomaton(fa, "abbbaaababa", true);
        checkAutomaton(fa, "aabbabbbaaabaaaabbb", true);
        checkAutomaton(fa, "aabbabbbb", true);
        checkAutomaton(fa, "babab", false);
        checkAutomaton(fa, "aaa", false);
        checkAutomaton(fa, "aabbaaabab", false);
        checkAutomaton(fa, "baba", true);
        checkAutomaton(fa, "aabb", true);
        checkAutomaton(fa, "baabbbaabbbbbaaaabbabbaaabbabbabbaabbbbbb", true);
        checkAutomaton(fa, "bababaabbbabaaaababb", true);
        checkAutomaton(fa, "ababbababbabababaabbbabbabbabaa", false);
        checkAutomaton(fa, "aaaabaabbab", false);
        checkAutomaton(fa, "baabbabaaaabbbbbbaab", false);
        checkAutomaton(fa, "bbaa", false);
    }

    @Test
    public void nfaEpsilonNullPoleETähtEgaEpsilonSümbol() {
        FiniteAutomaton fa = automaton(
                0,
                Collections.singletonList(1),
                tr(0, null, 1)
        );

        checkAutomaton(fa, "", true);
        checkAutomaton(fa, "e", false);
        checkAutomaton(fa, "ε", false);
    }

    @Test
    public void nfaKoosTsükliga0() {
        FiniteAutomaton fa = automaton(
                0,
                Collections.singletonList(1),
                tr(0, 'a', 0),
                tr(0, 'b', 0),
                tr(0, 'a', 1),
                tr(0, null, 0),
                tr(1, null, 1)
        );

        checkAutomaton(fa, "", false);
        checkAutomaton(fa, "a", true);
        checkAutomaton(fa, "b", false);
        checkAutomaton(fa, "abba", true);
        checkAutomaton(fa, "babbbbb", false);
        checkAutomaton(fa, "babbbbba", true);
        checkAutomaton(fa, "bbbbb", false);
        checkAutomaton(fa, "ababa", true);
        checkAutomaton(fa, "c", false);
        checkAutomaton(fa, "abcba", false);
        checkAutomaton(fa, "u", false);
    }

    @Test
    public void nfaKoosTsükliga1() {
        // (a|ε)*
        FiniteAutomaton fa = new FiniteAutomaton();
        fa.addState(0);
        fa.addState(2);
        fa.addState(3);
        fa.addState(1);
        fa.setStartState(0);
        fa.addAcceptingState(1);
        fa.addTransition(0, null, 1);
        fa.addTransition(0, null, 2);
        fa.addTransition(2, 'a', 3);
        fa.addTransition(2, null, 3);
        fa.addTransition(3, null, 1);
        fa.addTransition(3, null, 2);
        checkAutomaton(fa, "", true);
        checkAutomaton(fa, "aaaaaa", true);
        checkAutomaton(fa, "b", false);
        checkAutomaton(fa, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab", false);
    }

    @Test
    public void nfaKoosTsükliga2() {
        // (x|y*)*x
        FiniteAutomaton fa = new FiniteAutomaton();
        fa.addState(2);
        fa.addState(0);
        fa.addState(6);
        fa.addState(3);
        fa.addState(4);
        fa.addState(5);
        fa.addState(1);
        fa.setStartState(0);
        fa.addAcceptingState(1);
        fa.addTransition(2, 'x', 1);
        fa.addTransition(0, null, 3);
        fa.addTransition(0, null, 2);
        fa.addTransition(6, null, 5);
        fa.addTransition(6, null, 4);
        fa.addTransition(3, null, 5);
        fa.addTransition(3, null, 4);
        fa.addTransition(3, 'x', 4);
        fa.addTransition(4, null, 3);
        fa.addTransition(4, null, 2);
        fa.addTransition(5, 'y', 6);
        checkAutomaton(fa, "", false);
        checkAutomaton(fa, "yyxxxyx", true);
        checkAutomaton(fa, "yxyyxxyy", false);
        checkAutomaton(fa, "xyxyxyy", false);
        checkAutomaton(fa, "xyyyyxxxyxyy", false);
        checkAutomaton(fa, "yyyyyxyyyxxxyyxyxxxxxyxxxxxyyxxyyyx", true);
        checkAutomaton(fa, "yy", false);
        checkAutomaton(fa, "xyxxxxxxyyyyyxxxxxxyxyxxx", true);
        checkAutomaton(fa, "yyyxyxxxxyyxyyyy", false);
        checkAutomaton(fa, "yyxxyyxyxxxxxyxxxyyyyxyxyyxxyxxyxxyyx", true);
        checkAutomaton(fa, "xxyxyxxyyyxyx", true);
        checkAutomaton(fa, "xxxyy", false);
        checkAutomaton(fa, "xyyyyxyxy", false);
        checkAutomaton(fa, "xyxxyyyxyxxxxyxyxxxxxyyyxxxyxxyxxxxyyxyyyxxxyyyx", true);
        checkAutomaton(fa, "xyxyyxyxxxyyyyxyyxyxyxxxyxyxyxxxxyxxyxxyyyxyxyx", true);
        checkAutomaton(fa, "xyyyxxx", true);
        checkAutomaton(fa, "xxxxyxyyyyyxxxxyyxxxxyxyyyyyxxy", false);
        checkAutomaton(fa, "yxxyxyxy", false);
        checkAutomaton(fa, "yxyyyx", true);
        checkAutomaton(fa, "yxyxxxyyxxxxyxyxxyxxyxyyyxyxxxxxx", true);
        checkAutomaton(fa, "xxyxxyxxxxyyxyyy", false);
        checkAutomaton(fa, "yx", true);
        checkAutomaton(fa, "yyyxyyxxyyyyxxyxxyyyxyxxy", false);
        checkAutomaton(fa, "yyxxyxyxxxyxyxyyyxxxxxxxxxy", false);
        checkAutomaton(fa, "y", false);
    }

    @Test
    public void nfaKoosTsükliga3() {
        // ((a|c)*c(a|b*)*)*|b*bc*(b*a|ab)*
        FiniteAutomaton fa = new FiniteAutomaton();
        fa.addState(17);
        fa.addState(11);
        fa.addState(6);
        fa.addState(4);
        fa.addState(24);
        fa.addState(2);
        fa.addState(21);
        fa.addState(8);
        fa.addState(12);
        fa.addState(3);
        fa.addState(18);
        fa.addState(9);
        fa.addState(1);
        fa.addState(23);
        fa.addState(20);
        fa.addState(7);
        fa.addState(22);
        fa.addState(19);
        fa.addState(10);
        fa.addState(14);
        fa.addState(13);
        fa.addState(16);
        fa.addState(5);
        fa.addState(0);
        fa.addState(15);
        fa.setStartState(0);
        fa.addAcceptingState(1);
        fa.addTransition(17, 'c', 18);
        fa.addTransition(11, null, 9);
        fa.addTransition(11, null, 10);
        fa.addTransition(6, null, 5);
        fa.addTransition(6, null, 4);
        fa.addTransition(4, 'c', 7);
        fa.addTransition(24, 'b', 20);
        fa.addTransition(2, null, 5);
        fa.addTransition(2, null, 4);
        fa.addTransition(21, 'a', 20);
        fa.addTransition(8, 'a', 9);
        fa.addTransition(8, null, 9);
        fa.addTransition(8, null, 10);
        fa.addTransition(12, 'b', 15);
        fa.addTransition(3, null, 2);
        fa.addTransition(3, null, 1);
        fa.addTransition(18, null, 17);
        fa.addTransition(18, null, 16);
        fa.addTransition(9, null, 3);
        fa.addTransition(9, null, 8);
        fa.addTransition(23, null, 22);
        fa.addTransition(23, null, 21);
        fa.addTransition(20, null, 19);
        fa.addTransition(20, null, 1);
        fa.addTransition(7, null, 3);
        fa.addTransition(7, null, 8);
        fa.addTransition(22, 'b', 23);
        fa.addTransition(19, 'a', 24);
        fa.addTransition(19, null, 21);
        fa.addTransition(19, null, 22);
        fa.addTransition(10, 'b', 11);
        fa.addTransition(14, null, 13);
        fa.addTransition(14, null, 12);
        fa.addTransition(13, 'b', 14);
        fa.addTransition(16, null, 19);
        fa.addTransition(16, null, 1);
        fa.addTransition(5, 'c', 6);
        fa.addTransition(5, 'a', 6);
        fa.addTransition(0, null, 2);
        fa.addTransition(0, null, 13);
        fa.addTransition(0, null, 1);
        fa.addTransition(0, null, 12);
        fa.addTransition(15, null, 16);
        fa.addTransition(15, null, 17);
        checkAutomaton(fa, "", true);
        checkAutomaton(fa, "ccbcbccbacabacca", true);
        checkAutomaton(fa, "cccc", true);
        checkAutomaton(fa, "babbaccabbaccaaababcaccacccaabbcababccbbbbbbcabbcbababccaccabacaacccbaaaaacacb", false);
        checkAutomaton(fa, "cabcbccaabbacbcccbabbaacacacbcaccaacbb", true);
        checkAutomaton(fa, "bbbbbbcccacbbcaaaaabbbcaba", false);
        checkAutomaton(fa, "caabbcacbbbaacacbbabcbccbabcbcbaabc", true);
        checkAutomaton(fa, "baacb", false);
        checkAutomaton(fa, "aabbac", false);
        checkAutomaton(fa, "cca", true);
        checkAutomaton(fa, "bb", true);
        checkAutomaton(fa, "cbaababbabb", true);
        checkAutomaton(fa, "b", true);
        checkAutomaton(fa, "cbbcbbbabb", true);
        checkAutomaton(fa, "bbacaacb", false);
        checkAutomaton(fa, "bcbbccabbbabbcbbaaccaacc", false);
        checkAutomaton(fa, "cbabacccaaa", true);
        checkAutomaton(fa, "bcbbbbbb", false);
        checkAutomaton(fa, "babbabbccacc", false);
        checkAutomaton(fa, "bcaacacccababbabbcacccabcaaababa", false);
        checkAutomaton(fa, "cccbccaaa", true);
        checkAutomaton(fa, "aacabbacabbccabacbac", true);
        checkAutomaton(fa, "bccbcccc", false);
        checkAutomaton(fa, "baccacaabcb", false);
        checkAutomaton(fa, "bccbbbbbcaacbbaaba", false);
    }


    @Test
    public void nfaPikaSisendiga() {
        FiniteAutomaton fa = automaton(
                0,
                Collections.singletonList(0),
                tr(0, 'a', 1),
                tr(1, 'b', 0),
                tr(1, 'b', 1)
        );

        StringBuilder sb = new StringBuilder();
        sb.repeat("ababbabab", 1000);

        checkAutomaton(fa, sb.toString(), true);
        sb.append('a');
        checkAutomaton(fa, sb.toString(), false);
    }

    private static void checkAutomaton(FiniteAutomaton fa, String input, boolean expectedAccepts) {
        String aStr = fa.toString().replace("\r\n", "\n");

        lastTestDescription = "Automaadi toString:\n>"
                        + aStr.replaceAll("\n", "\n>")
                        + "\n\nSisend:"
                        +  (input.length() < 100 ? input : input.substring(0, 90) + "...<veel "
                                + (input.length()-90) + " sümbolit>")
                        + "\n";

        if (fa.accepts(input) != expectedAccepts) {
            fail("accepts peaks tagastama " + expectedAccepts
                    + ", aga tagastas " + !expectedAccepts);
        }
    }


    private static Transition tr(int from, Character label, int to) {
        return new Transition(from, label, to);
    }

    private static class Transition {
        final int from;
        final int to;
        final Character label;

        public Transition(int from, Character label, int to) {
            this.from = from;
            this.label = label;
            this.to = to;
        }

        public String toString() {
            return "(" + from + ")"
                    + " -[" + (label == null ? "ɛ" : label) + "]-> "
                    + "(" + to + ")";
        }
    }

    private static FiniteAutomaton automaton(Integer startState, List<Integer> acceptingStates,
                                             Transition... transitions) {
        FiniteAutomaton fa = new FiniteAutomaton();

        Set<Integer> addedStates = new HashSet<>();

        for (int state : acceptingStates) {
            fa.addState(state);
            fa.addAcceptingState(state);
            addedStates.add(state);
        }

        if (!addedStates.contains(startState)) {
            fa.addState(startState);
            addedStates.add(startState);
        }

        fa.setStartState(startState);


        for (Transition trans : transitions) {
            if (!addedStates.contains(trans.from)) {
                fa.addState(trans.from);
                addedStates.add(trans.from);
            }
            if (!addedStates.contains(trans.to)) {
                fa.addState(trans.to);
                addedStates.add(trans.to);
            }

            fa.addTransition(trans.from, trans.label, trans.to);
        }

        return fa;
    }

}
