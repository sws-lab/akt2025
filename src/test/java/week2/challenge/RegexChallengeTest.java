package week2.challenge;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.BasicOperations;
import dk.brics.automaton.RegExp;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegexChallengeTest {

    @Test
    public void testL1() {
        check(RegexChallenge.L1, RegexChallengeTask.TASKS[0].getAutomaton());
    }

    @Test
    public void testL2() {
        check(RegexChallenge.L2, RegexChallengeTask.TASKS[1].getAutomaton());
    }

    @Test
    public void testRE1() {
        check(RegexChallenge.RE1, RegexChallengeTask.TASKS[2].getAutomaton());
    }

    @Test
    public void testRE2() {
        check(RegexChallenge.RE2, RegexChallengeTask.TASKS[3].getAutomaton());
    }

    @Test
    public void testRE3() {
        check(RegexChallenge.RE3, RegexChallengeTask.TASKS[4].getAutomaton());
    }

    @Test
    public void testRE4() {
        check(RegexChallenge.RE4, RegexChallengeTask.TASKS[5].getAutomaton());
    }

    public static void check(String s, Automaton a) {
        Automaton q = (new RegExp(s, RegExp.NONE)).toAutomaton();
        String s1 = BasicOperations.minus(a, q).getShortestExample(true);
        if (s1 != null) fail("Sinu regex ei tunne ära sõna \"" + eps(s1) + "\".");
        String s2 = BasicOperations.minus(q, a).getShortestExample(true);
        if (s2 != null) fail("Sinu regex sobitub ka sõnaga \"" + eps(s2) + "\".");
    }

    private static String eps(String s) {
        if (s.isEmpty()) return "ε";
        else return s;
    }
}
