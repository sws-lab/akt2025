package week4;

import dk.brics.automaton.Automaton;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.BricsUtils;
import week3.FiniteAutomaton;
import week4.regex.RegexParser;
import week4.regex.ast.RegexNode;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DfaMinimizerTest {

    @Test
    public void test01_alt() {
        check("a|bc|d");
    }

    @Test
    public void test02_combos() {
        check("(a|b)*");
        check("(ab|cd)*");
    }

    @Test
    public void test03_epsilon() {
        check("(aε)*");
        check("aε|a|b");
    }

    @Test
    public void test04_madness() {
        check("(a|b)*b(a|b)");
        check("((a|b)*b(a|b))*|ab|b*");
        check("(a|b)*b(a|b)(x|bgg)*g(aεd)*fa(ga|ε)*");
        check("(((ε|b)*b(ε|ε|ε*|ε|ε))*|ab|b*)*");
    }

    @Test
    public void test05_book() {
        FiniteAutomaton fa = new FiniteAutomaton();
        fa.addState(0);
        fa.addState(1);
        fa.addState(2);
        fa.addState(3);
        fa.addState(4);
        fa.addState(5);
        fa.addState(6);
        fa.addState(7);
        fa.setStartState(0);
        fa.addAcceptingState(0);
        fa.addAcceptingState(6);

        fa.addTransition(0, 'a', 1);
        fa.addTransition(1, 'a', 4);
        fa.addTransition(1, 'b', 2);
        fa.addTransition(2, 'a', 3);
        fa.addTransition(2, 'b', 5);
        fa.addTransition(3, 'b', 1);
        fa.addTransition(4, 'a', 6);
        fa.addTransition(4, 'b', 5);
        fa.addTransition(5, 'a', 7);
        fa.addTransition(5, 'b', 2);
        fa.addTransition(6, 'a', 5);
        fa.addTransition(7, 'a', 0);
        fa.addTransition(7, 'b', 5);
        check(fa);
    }

    @Test
    public void test06_varmo() {
        check("(a|b)*abb");

        FiniteAutomaton fa = new FiniteAutomaton();
        fa.addState(0);
        fa.addState(1);
        fa.addState(2);
        fa.addState(3);
        fa.addState(4);
        fa.setStartState(0);
        fa.addAcceptingState(4);

        fa.addTransition(0, 'a', 1);
        fa.addTransition(0, 'b', 2);
        fa.addTransition(1, 'a', 1);
        fa.addTransition(1, 'b', 3);
        fa.addTransition(2, 'a', 1);
        fa.addTransition(2, 'b', 2);
        fa.addTransition(3, 'a', 1);
        fa.addTransition(3, 'b', 4);
        fa.addTransition(4, 'a', 1);
        fa.addTransition(4, 'b', 2);
        check(fa);
    }

    private void check(String re) {
        RegexNode root = RegexParser.parse(re);
        FiniteAutomaton nfa = Grep.regexToFiniteAutomaton(root);
        check(nfa);
    }

    private void check(FiniteAutomaton nfa) {
        FiniteAutomaton dfa = Grep.determinize(nfa);
        checkMinimization(dfa);
    }

    private void checkMinimization(FiniteAutomaton dfa) {
        FiniteAutomaton minimized = DfaMinimizer.minimize(dfa);

        assertEquivalent(dfa, minimized);
        assertMinimal(minimized);
    }

    private void assertEquivalent(FiniteAutomaton dfa, FiniteAutomaton minimized) {
        Automaton dfaBrics = BricsUtils.fromAktAutomaton(dfa);
        Automaton minimizedBrics = BricsUtils.fromAktAutomaton(minimized);
        if (!minimizedBrics.equals(dfaBrics)) {
            Automaton diff = dfaBrics.minus(minimizedBrics);
            if (diff.isEmpty())
                diff = minimizedBrics.minus(dfaBrics);
            fail("not equivalent for input '" + diff.getShortestExample(true) + "'");
        }
    }

    private static void assertMinimal(FiniteAutomaton minimized) {
        Automaton brics = BricsUtils.fromAktAutomaton(minimized);
        int actualStates = brics.getNumberOfStates();
        brics.minimize();
        int expectedStates = brics.getNumberOfStates();
        assertEquals("number of states in minimized automaton", expectedStates, actualStates);
    }
}
