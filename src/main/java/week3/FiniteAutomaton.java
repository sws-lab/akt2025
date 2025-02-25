package week3;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class FiniteAutomaton extends AbstractAutomaton {

    @Override
    public void addState(int state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStartState(int state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addAcceptingState(int state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addTransition(int fromState, Character label, int toState) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> getStates() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer getStartState() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> getAcceptingStates() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Character> getOutgoingLabels(int state) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Integer> getDestinations(int state, Character label) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean accepts(String input) {
        throw new UnsupportedOperationException();
    }

    /**
     * Seda meetodit ei hinnata ja seda ei pea muutma, aga läbikukkunud testide korral
     * antakse sulle automaadi kirjelduseks just selle meetodi tagastusväärtus.
     */
    @Override
    public String toString() {
        return super.toString();
    }

    public static void main(String[] args) throws IOException {
        FiniteAutomaton fa = new FiniteAutomaton();

        fa.addState(0);
        fa.addState(1);
        fa.addState(2);

        fa.addTransition(0, 'b', 0);
        fa.addTransition(0, 'c', 2);
        fa.addTransition(2, 'a', 1);
        fa.addTransition(1, 'd', 0);
        fa.addTransition(0, null, 1);

        fa.setStartState(0);
        fa.addAcceptingState(1);

        System.out.println(fa.accepts("cadbbbca")); // true
        System.out.println(fa.accepts("abc"));      // false
        System.out.println(fa.accepts(""));         // true

        // Pead ise veenduda, et toString töötab...
        System.out.println(fa);
        fa.renderPngFile(Paths.get("graphs", "auto.png"));
    }
}
