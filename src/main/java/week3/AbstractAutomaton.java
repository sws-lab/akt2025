package week3;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

/**
 * Abstraktne klass FiniteAutomaton implementeerimiseks.
 * Võimaldab automaadi joonistamist.
 */
public abstract class AbstractAutomaton {
    /**
     * Selle meetodiga annab automaadi koostaja teada, millised olekud automaadis
     * esinevad.
     */
    public abstract void addState(int state);

    /**
     * Kasulik abimeetod Grep kodutööks, mis loob uue seisundi ja tagastab selle ID.
     */
    public int addState() {
        Set<Integer> states = getStates();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (!states.contains(i)) {
                addState(i);
                return i;
            }
        }
        throw new RuntimeException("Too many states");
    }

    /**
     * Selle meetodiga määratakse algolek. Võib eeldada, et eelnevalt on see olek
     * automaati lisatud.
     */
    public abstract void setStartState(int state);

    /**
     * Selle meetodiga märgitakse olek lõppolekuks.
     */
    public abstract void addAcceptingState(int state);

    /**
     * Selle meetodiga lisatakse uus üleminek. Epsilon-ülemineku korral on label==null.
     * Võib eeldada, et olekud fromState ja toState on juba eelnevalt lisatud.
     */
    public abstract void addTransition(int fromState, Character label, int toState);

    /**
     * Tagastab kõigi lisatud olekute hulga.
     */
    public abstract Set<Integer> getStates();

    /**
     * Tagastab algoleku.
     */
    public abstract Integer getStartState();

    /**
     * Tagastab lõppolekud.
     */
    public abstract Set<Integer> getAcceptingStates();

    /**
     * Tagastab, millised üleminekud antud olekust väljuvad.
     */
    public abstract Set<Character> getOutgoingLabels(int state);

    /**
     * Tagastab, millistesse olekutesse antud olekust antud sümboliga saab.
     */
    public abstract Set<Integer> getDestinations(int state, Character label);

    /**
     * See meetod peab ütlema, kas automaat tunneb ära näidatud sisendi.
     */
    public abstract boolean accepts(String input);

    public void renderPngFile(Path path) throws IOException {
        AbstractAutomatonVisualizer.renderPngFile(this, path);
    }
}
