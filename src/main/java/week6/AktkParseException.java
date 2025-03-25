package week6;

import week5.AktkToken;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/*
 * Seda klassi ei ole vaja muuta ega esitada
 */
public class AktkParseException extends RuntimeException {
    private final AktkToken token;
    private final Set<AktkToken.Type> expected;

    public AktkToken getToken() {
        return token;
    }

    public Set<AktkToken.Type> getExpected() {
        return expected;
    }

    public AktkParseException(AktkToken tok, AktkToken.Type... expected) {
        super("Unexpected token '" + tok + "' but expected: " + Arrays.toString(expected));
        this.token = tok;
        this.expected = new LinkedHashSet<>(Arrays.asList(expected));
    }

    // Kui ei soovi täpsema veatöötlusega tegeleda.
    public AktkParseException() {
        token = null;
        expected = null;
    }

}
