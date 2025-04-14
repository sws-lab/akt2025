package week8.altbusinessrules;

import java.util.Map;

public record Person(String name, int age, String occupation, String degree, String gender) {

    public Map<String, String> toMap() {
        return Map.of(
                "name", name,
                "age", String.valueOf(age),
                "occupation", occupation,
                "degree", degree,
                "gender", gender);
    }
}
