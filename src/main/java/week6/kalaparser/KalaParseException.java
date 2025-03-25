package week6.kalaparser;

import week5.kalalexer.KalaToken;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class KalaParseException extends RuntimeException {
    private final KalaToken token;
    private final Set<KalaToken.Type> expected;

    public KalaToken getToken() {
        return token;
    }

    public Set<KalaToken.Type> getExpected() {
        return expected;
    }

    public KalaParseException(KalaToken tok, KalaToken.Type... expected) {
        super("Unexpected token '" + tok + "', but expected: " + Arrays.toString(expected));
        this.token = tok;
        this.expected = new LinkedHashSet<>(Arrays.asList(expected));
    }

}
