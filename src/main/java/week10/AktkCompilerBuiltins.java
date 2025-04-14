package week10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused")
public class AktkCompilerBuiltins {
    private static BufferedReader inputReader = null;

    // sisend-väljund
    public static int print(int value) {
        System.out.println(value);
        return 0; // mugavuse jaoks tagastame alati int
    }

    public static int readInt() {
        if (inputReader == null) {
            inputReader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        }

        try {
            return Integer.parseInt(inputReader.readLine());
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    // loogikatehted
    public static int not(int x) {
        return ~x;
    }

    public static int and(int a, int b) {
        return a & b;
    }

    public static int or(int a, int b) {
        return a | b;
    }

    // täisarvude tehted
    /**
     * Astendamine
     */
    public static int power(int x, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Ainult mittenegatiivne astendaja on lubatud");
        }

        int result = 1;
        while (n > 0) {
            result *= x;
            n--;
        }
        return result;
    }

    /**
     * Suurim ühistegur
     */
    public static int gcd(int a, int b) {
        while (b > 0) {
            int c = a % b;
            a = b;
            b = c;
        }
        return a;
    }
}
