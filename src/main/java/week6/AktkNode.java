package week6;

import java.util.Map;

import static week5.AktkToken.Type.*;

public abstract class AktkNode {

    public static AktkNode num(int value) {
        throw new UnsupportedOperationException();
    }

    public static AktkNode var(String name) {
        throw new UnsupportedOperationException();
    }

    public static AktkNode plus(AktkNode left, AktkNode right) {
        throw new UnsupportedOperationException();
    }

    public static AktkNode minus(AktkNode left, AktkNode right) {
        throw new UnsupportedOperationException();
    }

    public static AktkNode mul(AktkNode left, AktkNode right) {
        throw new UnsupportedOperationException();
    }

    public static AktkNode div(AktkNode left, AktkNode right) {
        throw new UnsupportedOperationException();
    }

    public abstract int eval(Map<String, Integer> env);

    public String toPrettyString() {
        return toString();
    }
}
