package week3.mealy;

public class TableEntry {
    private final int preState;
    private final char input;
    private final int postState;
    private final String output;

    /**
     * Mealy Masina üleminekutabeli rida. See on element hulgast S×Σ×S×Λ, kus S on seisundite hulk,
     * Σ on sisendtähestik ja Λ on väljundtähestik. Meil on konkreetsemalt S = int, Σ = char ja Λ on String.
     * Rea koosneb seega nelikust, mille komponendid on järgmised:
     * @param preState on seisund, millest võib teha antud üleminek.
     * @param input on sisendsümbol, mille puhul võidakse teha antud üleminek. Alakriips '_' on siin erilise
     *              tähendusega ja tähistab kõiki tähti.
     * @param postState on seisund, kuhu siirdutakse.
     * @param output on ülemineku väeljundtulemus. Selles väljundsõnes on samuti alakriips erilise tähendusega,
     *               nimelt asendatakse seda sisendsümboliga.
     */
    public TableEntry(int preState, char input, int postState, String output) {
        this.preState = preState;
        this.input = input;
        this.postState = postState;
        this.output = output;
    }

    public int getPostState() {
        return postState;
    }

    public String getOutput(char c) {
        return output.replace('_', c);
    }

    public boolean matches(int state, char c) {
        return state == preState && (input == '_' || input == c);
    }

}
