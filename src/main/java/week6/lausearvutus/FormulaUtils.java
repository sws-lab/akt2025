package week6.lausearvutus;

public class FormulaUtils {

    public static void main(String[] args) {
        String valem = "x&y∨r";
        System.out.println(parseFormula(valem));
    }

    /**
     * parseFormula peab tagastama sõnena antud lausearvutuse valemile
     * vastava andmestruktuuri.
     * <p>
     * Süntaktiliselt mittekorrektsete valemite korral tuleb visata erind.
     */
    public static Formula parseFormula(String formula) {
        throw new UnsupportedOperationException();
    }

    /**
     * moveNegations peab tagastama uue valemi, kus kõik eitused on
     * viidud vahetult lausemuutujate ette järgmiste reeglite põhjal:
     * <p>
     * ¬(¬F)  === F
     * ¬(F&G) === ¬F∨¬G
     * ¬(F∨G) === ¬F&¬G
     * ¬(F→G) === F&¬G
     * ¬(F↔G) === F↔¬G
     * <p>
     * NB! Antud reeglites tähistavad F ja G suvalisi valemeid, mitte tingimata
     * ainult lausemuutujaid.
     * <p>
     * NB! Kuigi eitusi saaks sisse viia ka teisiti, tuleks praegu kasutada
     * just neid teisendusi.
     */
    public static Formula moveNegations(Formula valem) {
        throw new UnsupportedOperationException();
    }
}
