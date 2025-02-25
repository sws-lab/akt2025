package week3.mealy;

import java.util.List;
import java.util.NoSuchElementException;

public class MealyMachine {

    private final List<TableEntry> table;

    /**
     * Loob Mealy masina üleminekurelatsiooni põhjal. Üleminekurelatsioon on esitatud üleminekute listina.
     * NB! Üleminkute järjekord on olulne: masin teeb esimese ülemineku, mis antud sisendile jaoks sobib.
     * Kui kasutatakse wildcard üleminekut, siis peab ise jälgima, et see esineks viimasena. Näiteks jääb
     * järgmine masin alati seisundisse 0, sest teine üleminek kunagi ei aktiveeru:
     *     new MealyMachine(Arrays.asList(
     *        new TableEntry(0, '_', 0, "_"),
     *        new TableEntry(0, '<', 1, ""),
     *        ...
     *     ));
     */
    public MealyMachine(List<TableEntry> table) {
        this.table = table;
    }

    public String run(String input) {
        StringBuilder sb = new StringBuilder();
        int state = 0;
        for (char c : input.toCharArray()) {
            TableEntry entry = findEntry(state, c);
            state = entry.getPostState();
            sb.append(entry.getOutput(c));
        }
        return sb.toString();
    }

    private TableEntry findEntry(int state, char input) {
        for (TableEntry entry : table) {
            if (entry.matches(state, input)) return entry;
        }
        throw new NoSuchElementException();
    }
}

