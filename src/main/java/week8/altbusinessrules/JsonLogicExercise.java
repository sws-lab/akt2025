package week8.altbusinessrules;

import io.github.jamsesso.jsonlogic.JsonLogic;
import io.github.jamsesso.jsonlogic.JsonLogicException;
import org.intellij.lang.annotations.Language;

import java.util.HashSet;
import java.util.Set;

public class JsonLogicExercise {

    // https://courses.cs.ut.ee/t/akt/Main/BusinessRules

    // Kõigepealt väike näide JSON-loogikast ja selle kasutamisest.
    // Defineerime siin kõik sõned ühe Java faili sees, et oleks mugavam esitada.
    // Järgmine on siis üks palga arvutamise reegel, mis sõltub inimese ametist ja haridusest.
    // NB! See on võetud ideaalsest maailmast, kus testimist väärtustatakse ja küberkuritegevus elimineeritud.
    @Language("JSON")
    public static String salaryJsonLogic = """
            {"*": [
              {"if" : [
                {"==": [{"var":"occupation"}, "programmer"] }, 1100,
                {"==": [{"var":"occupation"}, "designer"] }, 1300,
                {"==": [{"var":"occupation"}, "tester"] }, 1400,
                1000
              ]},
              {"if" :  [
                {"==": [{"var":"degree"}, "bsc"] }, 2.1,
                {"==": [{"var":"degree"}, "msc"] }, 2.5,
                {"==": [{"var":"degree"}, "phd"] }, 3.4,
                1.2
              ]}
            ]}
            """;

    // Nüüd saame reeglit rakendada järgmiselt:
    public static double computeSalary(Person person) throws JsonLogicException  {
        JsonLogic jsonLogic = new JsonLogic();
        return (double) jsonLogic.apply(salaryJsonLogic, person.toMap());
    }

    public static void main(String[] args) throws JsonLogicException {
        Person johann = new Person("Johann", 18, "programmer", "bsc", "male");
        System.out.println(computeSalary(johann));
        Person mari = new Person("Mari", 27, "tester", "phd", "male");
        System.out.println(computeSalary(mari));
    }


    // 1. ülesanne: kirjutage JSON-loogika reegel, mis arvutab välja soodustust.
    // NB! Ülesannete leht on siin: https://courses.cs.ut.ee/t/akt/Main/BusinessRules
    @Language("JSON")
    public static String discountJsonLogic = """
    """;


    // 2. ülesanne: kirjutage JSON-loogika reegel, mis paneb sõnad kokku, mis sisaldavad tähte 'a'.
    @Language("JSON")
    public static String wordsJsonLogic = """
    """;


    // 3. ülesanne: Defineeri meetod, mis tagastab kõik muutujad, millest antud reegel sõltub.
    public static Set<String> analyzeJsonRule(String jsonString) {
        return Set.of();
    }
}
