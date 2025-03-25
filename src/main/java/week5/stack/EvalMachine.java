package week5.stack;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class EvalMachine {

    private final Deque<Integer> stack;
    private int sign;
    private int sum;
    private boolean wasValue;
    private final Map<Character, Integer> env;

    public boolean wasValue() {
        return wasValue;
    }

    public EvalMachine(Map<Character, Integer> env) {
        stack = new ArrayDeque<>();
        sign = 1;
        stack.push(1); // <-- Mida võiks magasini peal jälgida??
        sum = 0;
        wasValue = false;
        this.env = env;
    }

    // Siin on vaja muuta/lisada neli rida, et see töötaks ka sulgudega:
    public int compute(String token) {
        switch (token) {
            case "(":
                break;
            case ")":
                break;
            case "-":
                sign *= -1;
            case "+":
                wasValue = false;
                break;
            default:
                if (wasValue) throw new RuntimeException("Expected operator!");
                sum += sign * getValue(token);
                wasValue = true;
                sign = 1;
        }
        return sum;
    }

    private int getValue(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            char variableName = token.charAt(0);
            return env.get(variableName);
        }
    }

}
