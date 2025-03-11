package week4;

import dk.brics.automaton.Automaton;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.BricsUtils;
import week3.FiniteAutomaton;
import week4.regex.ast.RegexNode;
import week4.regex.RegexParser;

import java.util.Set;

import static org.junit.Assert.fail;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GrepTest {
    public static String lastTestDescription = "";

    @Test
    public void test1_concat() {
        checkRegex("abcde");
        checkRegex("abe");
    }

    @Test
    public void test2_alt() {
        checkRegex("a|bc");
        checkRegex("a|bc|d");
    }

    @Test
    public void test3_star() {
        checkRegex("a*");
        checkRegex("a*b*");
        checkRegex("a*b");
        checkRegex("a*|b");
    }


    @Test
    public void test4_combos() {
        checkRegex("(ab)*");
        checkRegex("(a|b)*");
        checkRegex("(ab|cd)*");
        checkRegex("(ab)*|cd");
        checkRegex("(ab)*|(cd)*");
    }

    @Test
    public void test5_epsilon() {
        checkRegex("ε");
        checkRegex("ε*");
        checkRegex("aε");
        checkRegex("εa");
        checkRegex("ε|b");
        checkRegex("εa|b");
        checkRegex("(aε)*");
    }

    @Test
    public void test6_madness() {
        checkRegex("(a|b)*b(a|b)");
        checkRegex("((a|b)*b(a|b))*|ab|b*");
        checkRegex("(a|b)*b(a|b)(x|bgg)*g(aεd)*fa(ga|ε)*");
        checkRegex("(aε)*|a*|b*");
        checkRegex("((ε*)*(a*)*)*");
        checkRegex("(((ε|b)*b(ε|ε|ε*|ε|ε))*|ab|b*)*");
    }

    @Test
    public void test7_detAutomata() {

        // Juba determineeritud
        FiniteAutomaton a0 = new FiniteAutomaton();
        int a0s1 = a0.addState();
        int a0s2 = a0.addState();
        a0.addTransition(a0s1, 'a', a0s2);
        a0.setStartState(a0s1);
        a0.addAcceptingState(a0s2);
        checkDeterminization(a0);

        FiniteAutomaton a1 = new FiniteAutomaton();
        int a1s1 = a1.addState();
        int a1s2 = a1.addState();
        a1.addTransition(a1s1, 'a', a1s2);
        a1.addTransition(a1s1, null, a1s2);
        a1.setStartState(a1s1);
        a1.addAcceptingState(a1s2);
        checkDeterminization(a1);

        FiniteAutomaton a2 = new FiniteAutomaton();
        int a2s1 = a2.addState();
        int a2s2 = a2.addState();
        a2.addTransition(a2s1, 'a', a2s2);
        a2.addTransition(a2s1, 'a', a2s1);
        a2.setStartState(a2s1);
        a2.addAcceptingState(a2s2);
        checkDeterminization(a2);

        FiniteAutomaton a3 = new FiniteAutomaton();
        int a3s1 = a3.addState();
        int a3s2 = a3.addState();
        int a3s3 = a3.addState();
        a3.setStartState(a3s1);
        a3.addAcceptingState(a3s3);
        a3.addTransition(a3s1, null, a3s2);
        a3.addTransition(a3s2, null, a3s1);
        a3.addTransition(a3s1, 'a', a3s3);
        a3.addTransition(a3s2, 'b', a3s3);
        checkDeterminization(a3);
    }

    @Test
    public void test8_everything() {
        checkDeterminization("(a|b)*b(a|b)");
        checkDeterminization("ε");
        checkDeterminization("((a|b)*b(a|b))*|ab|b*");
        checkDeterminization("aε|a|b");
        checkDeterminization("(a|b)*b(a|b)(x|bgg)*g(aεd)*fa(ga|ε)*");
    }





    protected FiniteAutomaton regexToFiniteAutomaton(RegexNode root) {
        return Grep.regexToFiniteAutomaton(root);
    }

    private FiniteAutomaton checkRegex(String re) {
        lastTestDescription = "Regex: " + re;

        RegexNode root = RegexParser.parse(re);

        FiniteAutomaton auto = regexToFiniteAutomaton(root);
        Automaton actualBrics   = BricsUtils.fromAktAutomaton(auto);
        Automaton expectedBrics = BricsUtils.fromRegex(re);

        if (!actualBrics.equals(expectedBrics)) {
            Automaton diff = expectedBrics.minus(actualBrics);

            if (diff.isEmpty()) {
                diff = actualBrics.minus(expectedBrics);
            }

            fail("Sinu Grep-i poolt koostatud automaat ei anna õiget vastust sisendiga '"
                    + diff.getShortestExample(true) + "'");
        }
        return auto;
    }


    private void checkDeterminization(String re) {
        FiniteAutomaton auto = checkRegex(re);
        checkDeterminization(auto);
    }

    private void checkDeterminization(FiniteAutomaton automaton) {
        lastTestDescription = "Esialgne automaat:\n" + automaton;

        FiniteAutomaton determinized = Grep.determinize(automaton);

        if (determinized == automaton) {
            fail("Grep.determinize tagastas sama automaadi");
        }
        else if (!isDeterministic(determinized)) {
            fail("Grep.determinize tagastas automaadi, mis pole determineeritud:\n" + determinized);
        }
        else {
            Automaton originalBrics = BricsUtils.fromAktAutomaton(automaton);
            Automaton determinizedBrics = BricsUtils.fromAktAutomaton(determinized);
            if (! determinizedBrics.equals(originalBrics)) {
                Automaton diff = originalBrics.minus(determinizedBrics);

                if (diff.isEmpty()) {
                    diff = determinizedBrics.minus(originalBrics);
                }

                fail ("Grep.determinize poolt tagastatud automaat pole esialgsega samaväärne, "
                        + "erinevus tuleb näiteks sisendi '" + diff.getShortestExample(true) + "' korral:\n\n"
                        + determinized);
            }
        }
    }

    public boolean isDeterministic(FiniteAutomaton automaton) {
        for (Integer state : automaton.getStates()) {
            Set<Character> definedInputs = automaton.getOutgoingLabels(state);
            if (definedInputs.contains(null)) return false;
            for (Character input : definedInputs) {
                if (automaton.getDestinations(state, input).size() > 1) return false;
            }
        }
        return true;
    }

}
