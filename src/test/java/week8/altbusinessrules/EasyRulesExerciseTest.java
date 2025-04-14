package week8.altbusinessrules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static week8.altbusinessrules.EasyRulesExercise.createDiscountRules;
import static week8.altbusinessrules.EasyRulesExercise.createFizzbuzzRules;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EasyRulesExerciseTest {

    @Test
    public void test04_fizzBuzzRules() {
        Facts facts = new Facts();
        RulesEngine rulesEngine = new DefaultRulesEngine();
        Rules fizzbuzzRules = createFizzbuzzRules();

        StringBuilder stringBuilder = new StringBuilder();
        facts.put("sb", stringBuilder);


        facts.put("number", 9);
        rulesEngine.fire(fizzbuzzRules, facts);
        assertEquals("fizz", stringBuilder.toString());

        facts.put("number", 12);
        rulesEngine.fire(fizzbuzzRules, facts);
        assertEquals("fizzfizz", stringBuilder.toString());

        facts.put("number", 65);
        rulesEngine.fire(fizzbuzzRules, facts);
        assertEquals("fizzfizzbuzz", stringBuilder.toString());

        facts.put("number", 105);
        rulesEngine.fire(fizzbuzzRules, facts);
        assertEquals("fizzfizzbuzzfizzbuzz", stringBuilder.toString());
    }

    @Test
    public void test05_createDiscountRules() {
        Facts facts = new Facts();
        Cashier cashier = new Cashier();
        facts.put("cashier", cashier);

        RulesEngine rulesEngine = new DefaultRulesEngine();
        Rules rules1 = createDiscountRules(List.of(
                new EasyRulesExercise.TableEntry(200.0, 1000.0,0.05),
                new EasyRulesExercise.TableEntry(1000.0, 1500.0, 0.10),
                new EasyRulesExercise.TableEntry(1500.0, null,0.20)
        ));

        facts.put("spent", 250.0);
        facts.put("price", 80.0);
        rulesEngine.fire(rules1, facts);
        assertEquals(0.05, cashier.getDiscount(), 0.01);
        assertEquals(76.0, cashier.getPayment(), 0.01);

        rulesEngine.fire(rules1, facts);
        facts.put("spent", 1300.0);
        assertEquals(0.05, cashier.getDiscount(), 0.01);
        assertEquals(76.0, cashier.getPayment(), 0.01);

        Rules rules2 = createDiscountRules(List.of(
                new EasyRulesExercise.TableEntry(200.0, 1200.0, 0.05),
                new EasyRulesExercise.TableEntry(1200.0, 1600.0, 0.15),
                new EasyRulesExercise.TableEntry(1600.0, null, 0.26)
        ));
        rulesEngine.fire(rules2, facts);
        assertEquals(0.15, cashier.getDiscount(), 0.01);
        assertEquals(68.0, cashier.getPayment(), 0.01);

        facts.put("spent", 1700.0);
        rulesEngine.fire(rules2, facts);
        assertEquals(0.26, cashier.getDiscount(), 0.01);
        assertEquals(59.2, cashier.getPayment(), 0.01);
    }
}
