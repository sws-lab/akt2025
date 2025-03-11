package utils;

import dk.brics.automaton.*;
import week3.AbstractAutomaton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BricsUtils {

    public static Automaton fromAktAutomaton(AbstractAutomaton source) {
        Automaton automaton = new Automaton();
        Map<Integer, State> states = new HashMap<>();
        Set<StatePair> epsilons = new HashSet<>();

        for (Integer stateId : source.getStates()) {
            State state = new State();
            state.setAccept(source.getAcceptingStates().contains(stateId));
            states.put(stateId, state);
        }

        for (Integer fromId : source.getStates()) {
            for (Character label : source.getOutgoingLabels(fromId)) {
                for (Integer toId : source.getDestinations(fromId, label)) {
                    State fromState = states.get(fromId);
                    State toState = states.get(toId);
                    if (label == null) epsilons.add(new StatePair(fromState, toState));
                    else fromState.addTransition(new Transition(label, toState));
                }
            }
        }

        State initialState = states.get(source.getStartState());
        automaton.setInitialState(initialState);
        automaton.addEpsilons(epsilons);
        automaton.restoreInvariant();
        automaton.setDeterministic(false);
        return automaton;
    }


    public static Automaton fromRegex(String regex) {
        String str = regex.replace("ε", "()");
        Automaton automaton = new RegExp(str).toAutomaton();
        return automaton;
    }

}
