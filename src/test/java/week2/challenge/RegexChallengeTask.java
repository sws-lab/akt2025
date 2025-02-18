package week2.challenge;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.Arrays;

import static dk.brics.automaton.Automaton.*;
import static dk.brics.automaton.BasicOperations.repeat;
import static dk.brics.automaton.ShuffleOperations.shuffle;

public class RegexChallengeTask {

    public static final RegexChallengeTask[] TASKS = {

            new RegexChallengeTask("L1: Loengus lahendatud näide", "Arvsõned, mis ei ole 42.",
                    new RegExp("0*42").toAutomaton().complement().intersection(makeCharSet("0123456789").repeat(1))),

            new RegexChallengeTask("L2: Loengu ülesanne, mis jäi iseseisvaks lahendamiseks", "Arvsõned, mis on rangelt suuremad kui 42.",
                    gt(42)),

            new RegexChallengeTask("RE1: Praktikumi näidisülesanne", "Sõnad, milles esinevad täpselt üks '1' ja vähemalt üks '0'.",
                    shuffle(makeChar('1'), repeat(makeChar('0'), 1))),

            new RegexChallengeTask("RE2: Kursuse läbimiseks miinimum", "Sõnad, mis sisaldavad paarisarv a-sid ja paarisarv b-sid.",
                    shuffle(paaris('a'), paaris('b'))),

            new RegexChallengeTask("RE3: Paras väljakutse", "Ühtede ja nullidega sõned, mis ei sisalda 101.",
                    makeStringMatcher("101").complement().intersection(makeCharSet("01").repeat())),

            new RegexChallengeTask("RE4: Google-i tööintervjuu", "Kolmega jaguvad binaararvud.",
                    makeMod(0, 3))
    };

    public static String[] getNames() {
        return Arrays.stream(TASKS).map(RegexChallengeTask::getName).toArray(String[]::new);
    }

    private final String name;
    private final String description;
    private final Automaton automaton;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Automaton getAutomaton() {
        return automaton;
    }

    public RegexChallengeTask(String name, String description, Automaton automaton) {
        this.name = name;
        this.description = description;
        this.automaton = automaton;
    }


    private static Automaton makeMod(int r, int m) {
        Automaton automaton = new Automaton();
        State[] states = new State[m];
        for (int i = 0; i < states.length; i++)
            states[i] = new State();
        for (int i = 0; i < m; i++) {
            State s = states[i];
            s.addTransition(new Transition('0', states[ (2*i)   % m ]));
            s.addTransition(new Transition('1', states[ (2*i+1) % m ]));
        }
        states[r].setAccept(true);
        State init = new State();
        init.addTransition(new Transition('0', states[0]));
        init.addTransition(new Transition('1', states[1]));
        automaton.setInitialState(init);
        automaton.minimize();
        return automaton;
    }


    private static Automaton gt(int r) {
        Automaton automaton = new Automaton();
        State[] states = new State[r+1];
        State acc = new State();
        acc.addTransition(new Transition('0', '9', acc));
        acc.setAccept(true);
        for (int i = 0; i < states.length; i++) states[i] = new State();
        for (int i = 0; i <= r; i++) {
            State s = states[i];
            for (int j = 0; j < 10; j++) {
                char c = Character.forDigit(j, 10);
                int dest = 10*i+j;
                if (dest > r) s.addTransition(new Transition(c, acc));
                else s.addTransition(new Transition(c, states[dest]));
            }
        }
        automaton.setInitialState(states[0]);
        return automaton;
    }

    private static Automaton paaris(char c) {
        return repeat(repeat(makeChar(c), 2, 2));
    }

}
