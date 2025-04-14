package week8.altbusinessrules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;

import java.util.List;

public class EasyRulesExercise {

    // Kõigepealt näide EasyRules kasutamisest
    // https://courses.cs.ut.ee/t/akt/Main/BusinessRules
    public static void main(String[] args) {
        Rules discountRules = new Rules();

        Rule rule1 = new MVELRule()
                .name("5% sale")
                .description("If customer has spent between 200€-1000€, apply 5% discount")
                .priority(1)
                .when("200 <= spent && spent < 1000")
                .then("cashier.discount = 0.05");

        Rule rule2 = new MVELRule()
                .name("10% sale")
                .description("If customer has spent 1000€ or more, apply 10% discount")
                .priority(1)
                .when("1000 <= spent")
                .then("cashier.discount = 0.1");

        Rule rule3 = new MVELRule()
                .name("compute price")
                .description("Apply discount to cost")
                .priority(2)
                .when("true")
                .then("cashier.payment = price * (1 - cashier.discount)");

        discountRules.register(rule1, rule2, rule3);

        Facts facts = new Facts();
        Cashier cashier = new Cashier();
        facts.put("spent", (double) 20);
        facts.put("price", (double) 100);
        facts.put("cashier", cashier);

        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(discountRules, facts);

        System.out.println(cashier.getPayment());
    }


    // 4. ülesanne: Easyrules FizzBuzz
    public static Rules createFizzbuzzRules() {
        Rules rules = new Rules();
        return rules;
    }


    // 5. ülesanne: allahindluse reeglid on antud tabelina
    public record TableEntry(double spentMin, Double spentMax, double discount) {
    }

    public static Rules createDiscountRules(List<TableEntry> table) {
        Rules rules = new Rules();
        return rules;
    }

}
