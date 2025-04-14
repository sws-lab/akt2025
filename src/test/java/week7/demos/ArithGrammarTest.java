package week7.demos;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;
import utils.ExceptionErrorListener;

import static org.junit.Assert.*;

public class ArithGrammarTest {

    @Test
    public void test_literal() {
        legal("5+5");
        illegal("56++");
    }

    @Test
    public void test_variable() {
        legal("x+x*x");
    }


    private void legal(String input) {
        parse(input);
    }

    private void illegal(String input) {
        try {
            parse(input);
            fail("expected parse error: " + input);
        } catch (Exception ignored) {

        }
    }

    private void parse(String input) {
        ArithLexer lexer = new ArithLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        ArithParser parser = new ArithParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        parser.expr();
    }
}
