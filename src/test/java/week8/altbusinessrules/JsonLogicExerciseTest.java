package week8.altbusinessrules;

import io.github.jamsesso.jsonlogic.JsonLogic;
import io.github.jamsesso.jsonlogic.JsonLogicException;
import org.intellij.lang.annotations.Language;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static week8.altbusinessrules.JsonLogicExercise.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JsonLogicExerciseTest {

    private static double computeDiscount(double spent, double price) throws JsonLogicException {
        JsonLogic jsonLogic = new JsonLogic();
        Map<String, Double> data = Map.of(
                "spent", spent,
                "price", price
        );
        return (double) jsonLogic.apply(discountJsonLogic, data);
    }

    @Test
    public void test01_discount() throws Exception {
        assertEquals(90, computeDiscount(1000, 100), 0.01);
        assertEquals(450, computeDiscount(1200, 500), 0.01);
        assertEquals(95, computeDiscount(465, 100), 0.01);
    }

    private static String filterWords(String... words) throws JsonLogicException {
        JsonLogic jsonLogic = new JsonLogic();
        Map<String, String[]> data = Map.of("words", words);
        return (String) jsonLogic.apply(wordsJsonLogic, data);
    }

    @Test
    public void test02_filterWords() throws Exception {
        assertEquals("salakala", filterWords("sala", "koer", "kala"));
        assertEquals("kartulisalat", filterWords("kartuli", "püree", "salat", "kivi"));
    }

    @Test
    public void test03_analyzeJsonRule() {
        assertEquals(Set.of("occupation", "degree"), analyzeJsonRule(salaryJsonLogic));

        @Language("JSON")
        String jsonString = """
                {"*": [
                  {"if" : [
                    {"==": [{"var":"occupation"}, "programmer"] }, 1100,
                    {"==": [{"var":"occupation"}, "designer"] }, 1300,
                    {"==": [{"var":"occupation"}, "tester"] }, 1400,
                    1000
                  ]},
                  {"if" :  [
                    {"==": [{"var":"gender"}, "male"] }, 1.3,
                    {"==": [{"var":"gender"}, "female"] }, 1.0,
                    0.8
                  ]}
                ]}
                """;
        assertEquals(Set.of("occupation", "gender"), analyzeJsonRule(jsonString));
    }
}
