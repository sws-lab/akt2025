package week1;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MiniAktk {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new RuntimeException("Exactly one command line argument (path) required");
        }

        Path path = Paths.get(args[0]);
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            Map<Character, Integer> environment = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null) {
                line = removeComment(line);

                if (line.startsWith("print ")) { // print järel peab olema tühik
                    String expression = line.substring(6);
                    System.out.println(evaluateExpression(expression, environment));
                }
                else if (line.contains("=")) {
                    String[] parts = line.split("=");
                    if (parts.length != 2) {
                        throw new RuntimeException("Malformed assignment");
                    }
                    String trimmed = parts[0].trim();
                    if (trimmed.length() != 1) {
                        throw new RuntimeException("Variable name must be 1 character");
                    }
                    char variableName = trimmed.charAt(0);
                    if (!Character.isLetter(variableName)) {
                        throw new RuntimeException("Variable name must be a letter");
                    }
                    String expression = parts[1];
                    environment.put(variableName, evaluateExpression(expression, environment));
                }
                else if (!line.trim().isEmpty()) { // tühi rida pole viga
                    throw new RuntimeException("Malformed line: " + line);
                }
            }
        }
    }


    /**
     * Eemaldab realt kommentaari, kui see seal on.
     */
    private static String removeComment(String line) {
        if (line.contains("#")) return line.substring(0, line.indexOf('#'));
        else return line;
    }

    private static int evaluateExpression(String expression, Map<Character, Integer> environment) {
        // Ümbritseme liitmised tühikutega, asendame lahutamise negatiivse arvu liitmisega,
        // näiteks teisendame "a +b-c" kujule "a + b + -c", siis saab arvud lihtsalt kokku liita!
        // (Eeldame, et esialgses avaldises on ainult märgita täisarvud)
        expression = expression.replace("+", " + ").replace("-", " + -");
        String[] summands = expression.split("\\+");

        int sum = 0;
        for (String summand : summands) {
            sum += evaluateSummand(summand.trim(), environment);
        }
        return sum;
    }

    /*
     * Väärtustab märgita või miinusega täisarvu või muutujanime.
     */
    private static int evaluateSummand(String summand, Map<Character, Integer> environment) {
        int sign = 1;
        summand = summand.trim();
        if (summand.startsWith("-")) {
            sign = -1;
            summand = summand.substring(1).trim();
        }
        // proovime täisarvuna tõlgendada, kui ei saa, siis peaks olema muutuja
        try {
            return sign * Integer.parseInt(summand);
        } catch (NumberFormatException e) {
            char variableName = summand.charAt(0);
            if (environment.containsKey(variableName)) {
                return sign * environment.get(variableName);
            }
            else {
                throw new RuntimeException("Variable " + variableName + " undefined");
            }
        }
    }

    /**
     * Alternatiivne lahendus: sümbolhaaval avaldise väärtustamine.
     * Võrdle Exercise3 eval näidislahendusega:
     *  * Miinustega arvestamiseks piisab sign muutuja lisamisest.
     *  * Vigade tuvastamiseks läheb vaja ebatriviaalseid abimuutujate kombinatsioone.
     */
    private static int evaluateExpressionAlt(String expression, Map<Character, Integer> environment) {
        // avaldise väärtustamiseks vajalikud muutujad
        int sum = 0;
        int sign = 1; // "märk" (1 või -1) millega järgmine number/muutuja liidetakse
        int current = 0;
        // vigaste avaldiste tuvastamiseks vajalikud muutujad
        boolean needOperand = true; // kas järgmine sümbol PEAB olema number või muutuja nimi?
        boolean acceptDigit = true; // kas järgmine sümbol VÕIB olla number?

        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c) && acceptDigit) {
                current = 10 * current + Character.getNumericValue(c);
                needOperand = false;
                // endiselt acceptDigit on true
            } else if (Character.isLetter(c) && needOperand) {
                current = environment.get(c);
                acceptDigit = needOperand = false;
            } else if ((c == '+' || c == '-') && !needOperand) {
                sum += sign * current;
                sign = c == '+' ? 1 : -1;
                current = 0;
                acceptDigit = needOperand = true;
            } else if (c == ' ') {
                if (!needOperand) {
                    acceptDigit = false;
                }
            } else {
                // raske öelda, mis nüüd täpselt viga oli
                throw new RuntimeException("Malformed expression");
            }
        }
        if (needOperand) {
            throw new RuntimeException("Expected operand at the end of expression");
        }

        return sum + sign * current;
    }
}
