package week3;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class FiniteAutomaton extends AbstractAutomaton {
    private final Map<Integer, Map<Character, Set<Integer>>> transitions = new HashMap<>();
    private Integer startState = null;
    private final Set<Integer> acceptingStates = new HashSet<>();

    @Override
    public void addState(int state) {
        transitions.put(state, new HashMap<>());
    }

    @Override
    public void setStartState(int state) {
        startState = state;
    }

    @Override
    public void addAcceptingState(int state) {
        acceptingStates.add(state);
    }

    @Override
    public void addTransition(int fromState, Character label, int toState) {
        Map<Character, Set<Integer>> fromMap = transitions.get(fromState);
        Set<Integer> toSet = fromMap.computeIfAbsent(label, k -> new HashSet<>());
        toSet.add(toState);
    }

    @Override
    public Set<Integer> getStates() {
        return transitions.keySet();
    }

    @Override
    public Integer getStartState() {
        return startState;
    }

    @Override
    public Set<Integer> getAcceptingStates() {
        return acceptingStates;
    }

    @Override
    public Set<Character> getOutgoingLabels(int state) {
        return transitions.get(state).keySet();
    }

    @Override
    public Set<Integer> getDestinations(int state, Character label) {
        return transitions.get(state).getOrDefault(label, Collections.emptySet());
    }

    @Override
    public boolean accepts(String input) {
        Set<Integer> currentStates = epsilonClosure(Collections.singleton(startState));
        for (char c : input.toCharArray()) {
            if (currentStates.isEmpty()) return false;
            currentStates = move(currentStates, c);
        }
        return !Collections.disjoint(currentStates, acceptingStates);
    }

    // Defineerime abifunktsioon step, millega saab nii sulund kui ka move defineerida.
    // See vaatab, kuhu võib etteantud tähega (muuhulgas epsiloniga) minna.
    private Set<Integer> step(Set<Integer> states, Character c) {
        Set<Integer> nextStates = new HashSet<>();
        for (Integer state : states) nextStates.addAll(getDestinations(state, c));
        return nextStates;
    }

    public Set<Integer> epsilonClosure(Set<Integer> states) {
        Set<Integer> closure = new HashSet<>();
        while (closure.addAll(states)) states = step(states, null);
        return closure;
        //return Fixpoints.closure(x -> step(x, null), states);
    }

    public Set<Integer> move(Set<Integer> states, Character c) {
        return epsilonClosure(step(states, c));
    }

    /**
     * Seda meetodit ei hinnata ja seda ei pea muutma, aga läbikukkunud testide korral
     * antakse sulle automaadi kirjelduseks just selle meetodi tagastusväärtus.
     */
    @Override
    public String toString() {
        return "trans: " + transitions + "\n" +
                "start: " + startState + ", end: " + acceptingStates;
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
