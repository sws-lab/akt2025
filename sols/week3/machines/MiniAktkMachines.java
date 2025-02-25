package week3.machines;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniAktkMachines {

    public static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();         // Tühi sõne tähistab seisundit ￢TOK
        for (char c : input.toCharArray()) {
            if (c == '#') break;
            if (c == '+' || c == '-' || c == '=' || c == ' ') {   // lisame siia '=' ja tühikud.
                if (!currentToken.isEmpty()) {
                    tokens.add(currentToken.toString());          // push
                    currentToken = new StringBuilder();
                }
                if (c != ' ') tokens.add(Character.toString(c));  // echo (v.a. tühikud)
            } else {
                currentToken.append(c);                           // acc
            }
        }
        // Ja lõpuks EOF puhul ka push seisundis TOK:
        if (!currentToken.isEmpty()) tokens.add(currentToken.toString());
        return tokens;
    }

    public static int compute(List<String> tokens, Map<Character, Integer> environment) {
        int sign = 1;
        int sum = 0;
        boolean wasValue = false; // was the previous token a value
        for (String token : tokens) {
            switch (token) {
                case "-":
                    sign *= -1;
                    // intentional fallthrough
                case "+":
                    wasValue = false;
                    break;
                default:
                    if (wasValue) throw new RuntimeException("Expected operator!");
                    sum += sign * getValue(token, environment);
                    sign = 1;
                    wasValue = true;
            }
        }
        if (!wasValue) throw new RuntimeException("Malformed expression!");
        return sum;
    }

    private static int getValue(String token, Map<Character, Integer> environment) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            char variableName = token.charAt(0);
            return environment.get(variableName);
        }
    }

    public static void main(String[] args) throws IOException {
        Map<Character, Integer> environment = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(args[0]), StandardCharsets.UTF_8)) {
            for (String line; (line = reader.readLine()) != null; ) {
                if (!line.isEmpty() && line.charAt(0) == ' ') throw new RuntimeException("Begins with space!");
                List<String> tokens = tokenize(line);
                if (tokens.isEmpty()) continue;
                String firstToken = tokens.get(0);
                if (firstToken.equals("print")) {
                    System.out.println(compute(tokens.subList(1, tokens.size()), environment));
                } else if (tokens.get(1).equals("=")) {
                    if (firstToken.length() > 1) throw new RuntimeException("Too long var!");
                    char variableName = firstToken.charAt(0);
                    if (!Character.isLetter(variableName)) throw new RuntimeException("Invalid var name.");
                    environment.put(variableName, compute(tokens.subList(2, tokens.size()), environment));
                } else {
                    throw new RuntimeException("Unexpected operation!");
                }
            }
        }
    }
}
