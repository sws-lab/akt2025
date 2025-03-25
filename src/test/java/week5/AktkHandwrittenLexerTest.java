package week5;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static week5.AktkToken.Type.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AktkHandwrittenLexerTest {
    public static String lastTestDescription = "";

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(1000, TimeUnit.MILLISECONDS));

    @Test
    public void test01_int() {
        checkBasic("7",
                new AktkToken(INTEGER, 7),
                new AktkToken(EOF));
        checkBasic("0",
                new AktkToken(INTEGER, 0),
                new AktkToken(EOF));
        checkBasic("35",
                new AktkToken(INTEGER, 35),
                new AktkToken(EOF));
    }

    @Test
    public void test02_intWhitespace() {
        checkBasic("7 ",
                new AktkToken(INTEGER, 7),
                new AktkToken(EOF));
        checkBasic(" 0",
                new AktkToken(INTEGER, 0),
                new AktkToken(EOF));
        checkBasic(" 35 ",
                new AktkToken(INTEGER, 35),
                new AktkToken(EOF));
        checkBasic("3 5",
                new AktkToken(INTEGER, 3),
                new AktkToken(INTEGER, 5),
                new AktkToken(EOF));
    }

    @Test
    public void test03_Expressions() {
        checkBasic("3+4",
                new AktkToken(INTEGER, 3),
                new AktkToken(PLUS),
                new AktkToken(INTEGER, 4),
                new AktkToken(EOF));

        checkBasic("3- 4",
                new AktkToken(INTEGER, 3),
                new AktkToken(MINUS),
                new AktkToken(INTEGER, 4),
                new AktkToken(EOF));

        checkBasic("13 *488",
                new AktkToken(INTEGER, 13),
                new AktkToken(TIMES),
                new AktkToken(INTEGER, 488),
                new AktkToken(EOF));

        checkBasic("78/ 2",
                new AktkToken(INTEGER, 78),
                new AktkToken(DIV),
                new AktkToken(INTEGER, 2),
                new AktkToken(EOF));

        checkBasic("x /3",
                new AktkToken(VARIABLE, "x"),
                new AktkToken(DIV),
                new AktkToken(INTEGER, 3),
                new AktkToken(EOF));

        checkBasic("78 / (13*488)",
                new AktkToken(INTEGER, 78),
                new AktkToken(DIV),
                new AktkToken(LPAREN),
                new AktkToken(INTEGER, 13),
                new AktkToken(TIMES),
                new AktkToken(INTEGER, 488),
                new AktkToken(RPAREN),
                new AktkToken(EOF));
    }

    @Test
    public void test04_varsKeywords() {
        checkBasic("if",
                new AktkToken(IF),
                new AktkToken(EOF));

        checkBasic("ifs",
                new AktkToken(VARIABLE, "ifs"),
                new AktkToken(EOF));
        
        checkBasic("while",
                new AktkToken(WHILE),
                new AktkToken(EOF));

        checkBasic("whilewhile",
                new AktkToken(VARIABLE, "whilewhile"),
                new AktkToken(EOF));
        
        checkBasic("var",
                new AktkToken(VAR),
                new AktkToken(EOF));

        checkBasic("varikatus",
                new AktkToken(VARIABLE, "varikatus"),
                new AktkToken(EOF));
    }


    @Test
    public void test05_varsNumsKeywords() {
        checkBasic("if while varu_2",
                new AktkToken(IF),
                new AktkToken(WHILE),
                new AktkToken(VARIABLE, "varu_2"),
                new AktkToken(EOF));

        checkBasic("12 if 45 ifs",
                new AktkToken(INTEGER, 12),
                new AktkToken(IF),
                new AktkToken(INTEGER, 45),
                new AktkToken(VARIABLE, "ifs"),
                new AktkToken(EOF));

        checkBasic("12\nif\n45 ifs",
                new AktkToken(INTEGER, 12),
                new AktkToken(IF),
                new AktkToken(INTEGER, 45),
                new AktkToken(VARIABLE, "ifs"),
                new AktkToken(EOF));
    }

    @Test
    public void test06_simpleStrings() {
        checkBasic("\"plrrrahh\"",
                new AktkToken(STRING, "plrrrahh"),
                new AktkToken(EOF));
        checkBasic("\"\"",
                new AktkToken(STRING, ""),
                new AktkToken(EOF));
        checkBasic("\"eGo\"",
                new AktkToken(STRING, "eGo"),
                new AktkToken(EOF));
        checkBasic("\".\"",
                new AktkToken(STRING, "."),
                new AktkToken(EOF));
    }

    @Test
    public void test07_Comments() {

        checkBasic("x 3 //\"plahh\"",
                new AktkToken(VARIABLE, "x"),
                new AktkToken(INTEGER, 3),
                new AktkToken(EOF));

        checkBasic("x 3 //\"plahh\"\ntere",
                new AktkToken(VARIABLE, "x"),
                new AktkToken(INTEGER, 3),
                new AktkToken(VARIABLE, "tere"),
                new AktkToken(EOF));

        checkBasic("x /*4 5 6*/ 3",
                new AktkToken(VARIABLE, "x"),
                new AktkToken(INTEGER, 3),
                new AktkToken(EOF));

        checkBasic("x /*4 /* * /5 6*/ 3",
                new AktkToken(VARIABLE, "x"),
                new AktkToken(INTEGER, 3),
                new AktkToken(EOF));

        //checkBasic("x  * /*/5 6*/ 3",
        checkBasic("x  * /*5 6*/ 3",
                new AktkToken(VARIABLE, "x"),
                new AktkToken(TIMES),
                new AktkToken(INTEGER, 3),
                new AktkToken(EOF));

        // kommentaarid nagu selline:
        var lookAtTheEnd = 0; //
        checkBasic("x 3 //\n\"plahh\"",
                new AktkToken(VARIABLE, "x"),
                new AktkToken(INTEGER, 3),
                new AktkToken(STRING, "plahh"),
                new AktkToken(EOF));
    }


    @Test
    public void test08_escapedSymbols() {
        checkBasic("\"plrah\\nh\"",
                new AktkToken(STRING, "plrah\nh"),
                new AktkToken(EOF));

        checkBasic("\"plrah\\\"h\"",
                new AktkToken(STRING, "plrah\"h"),
                new AktkToken(EOF));


        checkBasic(" 314-a +\"ka\\nla\"",
                new AktkToken(INTEGER, 314),
                new AktkToken(MINUS),
                new AktkToken(VARIABLE, "a"),
                new AktkToken(PLUS),
                new AktkToken(STRING, "ka\nla"),
                new AktkToken(EOF)
        );
    }

    @Test
    public void test09_Positions() {
        checkPositioned(" 314-a +\"ka\\nla\"",
                new AktkToken(INTEGER, 314, 1, 3),
                new AktkToken(MINUS, 4, 1),
                new AktkToken(VARIABLE, "a", 5, 1),
                new AktkToken(PLUS, 7, 1),
                new AktkToken(STRING, "ka\nla", 8, 8),
                new AktkToken(EOF, 16, 0)
        );
    }

    @Test
    public void test10_readOnlyNeeded() {
        LinkedHashMap<String, AktkToken> pieces = new LinkedHashMap<>();
        pieces.put("   \"xyz\" /* kalapala*/", new AktkToken(STRING, "xyz"));
        pieces.put("3 a", new AktkToken(INTEGER, 3));
        pieces.put("bc\0", new AktkToken(VARIABLE, "abc"));
        checkReadNextToken(pieces);
    }


    private void checkBasic(String input, AktkToken... expectedTokensArray) {
        lastTestDescription = "Sisend:\n>"
                + input.replaceAll("\\r\\n", "\n").replaceAll("\\n", "\n>");

        List<AktkToken> expectedTokens = Arrays.asList(expectedTokensArray);
        AktkHandwrittenLexer lexer = new AktkHandwrittenLexer(new StringReader(input));
        List<AktkToken> actualTokens = lexer.readAllTokens();

        if (!expectedTokens.equals(actualTokens)) {
            fail("Ootasin tulemuseks sellist lekseemide jada:\n"
                    + formatTokens(expectedTokens)
                    + "aga sain:\n"
                    + formatTokens(actualTokens));
        }
    }

    private void checkPositioned(String input, AktkToken... expectedTokensArray) {
        lastTestDescription = "Sisend:\n>"
                + input.replaceAll("\\r\\n", "\n").replaceAll("\\n", "\n>");

        List<AktkToken> expectedTokens = Arrays.asList(expectedTokensArray);
        AktkHandwrittenLexer lexer = new AktkHandwrittenLexer(new StringReader(input));
        List<AktkToken> actualTokens = lexer.readAllTokens();
        assertTrue("Offset puudu!", actualTokens.stream().noneMatch(AktkToken::noOffset));

        if (!expectedTokens.equals(actualTokens)) {
            fail("Ootasin tulemuseks sellist lekseemide jada:\n"
                    + formatTokens(expectedTokens)
                    + "aga sain:\n"
                    + formatTokens(actualTokens));
        }
    }

    private void checkReadNextToken(LinkedHashMap<String, AktkToken> pieces) {
        StringBuilder wholeInput = new StringBuilder();
        for (String key : pieces.keySet()) {
            wholeInput.append(key.replaceAll("\\u0000", ""));
        }

        lastTestDescription = "Kogu sisend:\n> " + wholeInput.toString().replaceAll("\\n", "\n>");


        StringBuilder providedInput = new StringBuilder();

        try {
            PipedReader reader = new PipedReader();
            PipedWriter writer = new PipedWriter(reader);
            AktkHandwrittenLexer lexer = new AktkHandwrittenLexer(reader);

            for (Map.Entry<String, AktkToken> entry : pieces.entrySet()) {
                String piece = entry.getKey();
                if (piece.endsWith("\0")) {
                    writer.write(piece.substring(0, piece.length() - 1));
                    writer.flush();
                    writer.close();
                } else {
                    writer.write(piece);
                    writer.flush();
                }

                providedInput.append(piece);

                AktkToken expectedToken = entry.getValue();

                try {
                    AktkToken actualToken = lexer.readNextToken();

                    if (!actualToken.equals(expectedToken)) {
                        fail("Ootasin token-it " + expectedToken + ", aga tuli " + actualToken);
                    }
                } catch (Error e) {
                    fail("Kui sisendisse oli antud '" + providedInput.toString().replaceAll("\\u0000", "<sisendi lõpp>")
                            + "', siis järgmise getNextToken()-iga tekkis probleem: "
                            + e.getMessage());
                }
            }

            if (!lexer.readNextToken().equals(new AktkToken(EOF))) {
                fail("Eeldasin, et peale sisendi lõppu annab getNextToken EOF-i");
            }


        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String formatTokens(List<AktkToken> tokens) {
        StringBuilder sb = new StringBuilder();

        for (AktkToken token : tokens) {
            sb.append(">").append(token).append("\n");
        }

        return sb.toString();
    }


}
