package week4;

import week3.FiniteAutomaton;
import week4.regex.RegexParser;
import week4.regex.ast.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.util.Collections.*;

public class Grep {
    /*
     * main meetodit ei ole vaja muuta.
     *
     * See meetod on siin vaid selleks, et anda käesolevale  harjutusele veidi
     * realistlikum kontekst. Aga tegelikult on see vaid mäng -- see programm ei
     * pretendeeri päeva kasulikuima programmi tiitlile. Päris elus kasuta päris grep-i.
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 2) {
            System.err.println(
                    """
                            Programm vajab vähemalt ühte argumenti: regulaaravaldist.
                            Teiseks argumendiks võib anda failinime (kui see puudub, siis loetakse tekst standardsisendist).
                            Failinime andmisel eeldatakse, et tegemist on UTF-8 kodeeringus tekstifailiga.
                            Rohkem argumente programm ei aktsepteeri.
                            """
            );
            System.exit(1);
        }

        RegexNode regex = RegexParser.parse(args[0]);
        FiniteAutomaton automaton = determinize(regexToFiniteAutomaton(regex));

        InputStream inputStream;
        if (args.length == 2) {
            inputStream = Files.newInputStream(Paths.get(args[1]));
        } else {
            inputStream = System.in;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            // kuva ekraanile need read, mis vastavad antud regulaaravaldisele/automaadile
            String line;
            while ((line = reader.readLine()) != null) {
                if (automaton.accepts(line)) {
                    System.out.println(line);
                }
            }
        }
    }

    /*
     * See meetod peab loenguslaididel toodud konstruktsiooni põhjal koostama ja tagastama
     * etteantud regulaaravaldisele vastava mittedetermineeritud lõpliku automaadi.
     * Selle meetodi korrektne implementeerimine on antud ülesande juures kõige tähtsam.
     *
     * (Sa võid selle meetodi implementeerimiseks kasutada abimeetodeid ja ka abiklasse,
     * aga ära muuda meetodi signatuuri, sest automaattestid eeldavad just sellise signatuuri
     * olemasolu.)
     *
     * (Selle ülesande juures pole põhjust kasutada vahetulemuste salvestamiseks klassivälju,
     * aga kui sa seda siiski teed, siis kontrolli, et see meetod töötab korrektselt ka siis,
     * kui teda kutsutakse välja mitu korda järjest.)
     */
    public static FiniteAutomaton regexToFiniteAutomaton(RegexNode regex) {
        FiniteAutomaton automaton = new FiniteAutomaton();
        Fragment fragment = new RegexVisitor<Fragment>() {
            @Override
            protected Fragment visit(Alternation alternation) {
                Fragment leftFragment = visit(alternation.getLeft());
                Fragment rightFragment = visit(alternation.getRight());
                int in = automaton.addState();
                int out = automaton.addState();
                automaton.addTransition(in, null, leftFragment.in);
                automaton.addTransition(in, null, rightFragment.in);
                automaton.addTransition(leftFragment.out, leftFragment.outLabel, out);
                automaton.addTransition(rightFragment.out, rightFragment.outLabel, out);
                return new Fragment(in, out, null);
            }

            @Override
            protected Fragment visit(Concatenation concatenation) {
                Fragment leftFragment = visit(concatenation.getLeft());
                Fragment rightFragment = visit(concatenation.getRight());
                automaton.addTransition(leftFragment.out, leftFragment.outLabel, rightFragment.in);
                return new Fragment(leftFragment.in, rightFragment.out, rightFragment.outLabel);
            }

            @Override
            protected Fragment visit(Epsilon epsilon) {
                int state = automaton.addState();
                return new Fragment(state, state, null);
            }

            @Override
            protected Fragment visit(Letter letter) {
                int state = automaton.addState();
                return new Fragment(state, state, letter.getSymbol());
            }

            @Override
            protected Fragment visit(Repetition repetition) {
                Fragment childFragment = visit(repetition.getChild());
                int state = automaton.addState();
                automaton.addTransition(state, null, childFragment.in);
                automaton.addTransition(childFragment.out, childFragment.outLabel, state);
                return new Fragment(state, state, null);
            }
        }.visit(regex);
        automaton.setStartState(fragment.in);
        int endState = automaton.addState();
        automaton.addAcceptingState(endState);
        automaton.addTransition(fragment.out, fragment.outLabel, endState);
        return automaton;
    }

    private static class Fragment {
        public final int in;
        public final int out;
        public final Character outLabel; // label for transition from out

        private Fragment(int in, int out, Character outLabel) {
            this.in = in;
            this.out = out;
            this.outLabel = outLabel;
        }
    }

    /**
     * See meetod peab looma etteantud NFA-le vastava DFA, st. etteantud
     * automaat tuleb determineerida.
     * Kui sa seda ei jõua teha, siis jäta see meetod nii, nagu ta on.
     */
    public static FiniteAutomaton determinize(FiniteAutomaton nfa) {
        return determinize(nfa, nfa.epsilonClosure(singleton(nfa.getStartState())));
    }

    // Abimeetod BrzozowskiMinimizer-i jaoks.
    public static FiniteAutomaton determinize(FiniteAutomaton nfa, Set<Integer> startingState) {
        // Loengu slaidide järgi. Kõigepealt S', F', T' on meil uues DFAs:
        FiniteAutomaton dfa = new FiniteAutomaton();

        // Algoleku s'_0 lisamine:
        dfa.setStartState(addNfaStatesetToDfa(nfa, startingState, dfa));

        // W (workQueue/Set: ülevaatusele minevad DFA seisunditele vastavad NFA seisundite hulgad)
        Queue<Set<Integer>> workQueue = new ArrayDeque<>(singletonList(startingState));

        // Peame kuidagi NFA seisundite hulgad vastavate DFA seisunditega kokku viima
        Map<Set<Integer>, Integer> nfa2dfa = new HashMap<>(singletonMap(startingState, dfa.getStartState()));

        while (!workQueue.isEmpty()) {
            // Lähteolekute hulk X
            Set<Integer> moveFrom = workQueue.remove();

            // Vastav DFA seisundi ID
            int from = nfa2dfa.get(moveFrom);

            // Vaatame, millistesse teistesse seisunditesse sellest DFA seisundist pääsema peaks
            // ning lisame selleks üleminekud DFA-sse
            for (char label : getOutgoingLabels(nfa, moveFrom)) {
                // Sihtolekute hulk Y
                Set<Integer> moveTo = nfa.move(moveFrom, label);

                // Sihtolekute hulgale vastava DFA seisundi leidmine:
                int to = nfa2dfa.computeIfAbsent(moveTo, key -> {
                    // NB! Tegemist on uue seisundite hulgaga, seega paneme selle tööjärjekorda!
                    workQueue.add(key);
                    // Lisame uue seisundi DFA-sse ja tagastame selle tulemuse, et vastavus läheks nfa2dfa sõnastikku.
                    return addNfaStatesetToDfa(nfa, key, dfa);
                });

                // Lõpuks saame ülemineku DFA-sse lisada
                dfa.addTransition(from, label, to);
            }
        }

        return dfa;
    }

    private static int addNfaStatesetToDfa(FiniteAutomaton nfa, Set<Integer> states, FiniteAutomaton dfa) {
        // Juhul kui NFA seisundite hulgas on lõppolek, siis vastav DFA seisund on ka lõppolek.
        boolean dfaAccepting = !Collections.disjoint(states, nfa.getAcceptingStates());
        int state = dfa.addState();
        if (dfaAccepting)
            dfa.addAcceptingState(state);
        return state;
    }

    private static Set<Character> getOutgoingLabels(FiniteAutomaton nfa, Set<Integer> from) {
        Set<Character> result = new HashSet<>();
        for (Integer state : from) result.addAll(nfa.getOutgoingLabels(state));
        // NB! Siin me kindlasti ei taha epsilon-servasid vaadata!
        result.remove(null);
        return result;
    }
}
