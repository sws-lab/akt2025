package week5;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static week5.AktkToken.Type.*;

public class AktkHandwrittenLexer {

    // Siin on meie poolt mall ette antud, millega saad kasutada praktikumist tuttavad meetodid.
    // Selle malli kasutamine ei ole kohustuslik, aga sisend antakse ette Reader liidese kaudu
    // ja viimase testi läbimiseks ei tohi sisendis liiga palju ette vaadata.

    private static final char TERMINATOR = '\0';
    private final Reader reader;
    private Character current;
    private int pos = 0;

    public AktkHandwrittenLexer(Reader input) {
        this.reader = input;
    }

    // See teeb meil testimise mugavamaks:
    public List<AktkToken> readAllTokens() {
        List<AktkToken> tokens = new ArrayList<>();

        AktkToken token;
        do {
            token = this.readNextToken();
            tokens.add(token);
        } while (token.getType() != EOF);

        return tokens;
    }

    // Saame kasutada näiteks järgmiselt:
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("inputs", "escaped.txt");
        System.out.println(Files.readString(path));
        System.out.println(new AktkHandwrittenLexer(Files.newBufferedReader(path, StandardCharsets.UTF_8)).readAllTokens());
    }


    // Siin on abimeetodid, mida soovitame kasutada.
    // Consume on sisuliselt nagu pos++ kalalekseri näites ehk läheb järgmise tähe juurde.
    private void consume() {
        if (current == TERMINATOR) {
            throw new RuntimeException("Reading passed terminator!");
        }
        read();
        pos++;
    }

    // Peek on see, mis tagastab hetkel vaadeldavat tähte.
    // (Kõige algul võib see täht olla puudu.)
    private char peek() {
        if (current == null) read();
        return current;
    }

    // See on pigem abimeetod, mis teostab tegeliku sisendist lugemist.
    // Meie enda lahenduses me seda otse ei kasuta. Kutsume ainult peek ja consume.
    private void read() {
        try {
            int i = reader.read();
            current = (i == -1) ? TERMINATOR : (char) i;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    /**
     * See on nüüd see oluline meetod, mida peab ise implementeerima!
     */
    public AktkToken readNextToken() {
        while (peek() != TERMINATOR) {
            if (Character.isDigit(peek())) {
                return readInteger();
            } else if (Character.isWhitespace(peek())) {
                consume();
            } else if (peek() == '"') {
                return readString();
            } else if (Character.isLetter(peek()) || peek() == '_') {
                return readVariableOrKeyword();
            } else if (peek() == '(') {
                consume();
                return singleCharToken(LPAREN);
            } else if (peek() == ')') {
                consume();
                return singleCharToken(RPAREN);
            } else if (peek() == '+') {
                consume();
                return singleCharToken(PLUS);
            } else if (peek() == '-') {
                consume();
                return singleCharToken(MINUS);
            } else if (peek() == '*') {
                consume();
                return singleCharToken(TIMES);
            } else if (peek() == '/') {
                consume();
                if (peek() == '/') {
                    consume();
                    skipSingleLineComment();
                } else if (peek() == '*') {
                    consume();
                    skipMultiLineComment();
                } else {
                    return singleCharToken(DIV);
                }
            } else {
                throw new RuntimeException("Unexpected symbol");
            }
        }
        return new AktkToken(EOF, pos, 0);
    }

    private AktkToken singleCharToken(AktkToken.Type type) {
        return new AktkToken(type, pos - 1, 1);
    }

    private void skipMultiLineComment() {
        do {
            while (peek() != '*') consume();
            consume();
        } while (peek() != '/');
        consume();
    }

    private void skipSingleLineComment() {
        while (peek() != '\n' && peek() != TERMINATOR) consume();
    }

    private AktkToken readVariableOrKeyword() {
        assert Character.isLetter(peek()) || peek() == '_';
        int initPos = this.pos;

        StringBuilder sb = new StringBuilder();
        sb.append(peek());
        consume();

        while ((Character.isLetter(peek())
                || Character.isDigit(peek())
                || peek() == '_')) {
            sb.append(peek());
            consume();
        }

        String content = sb.toString();
        AktkToken token = switch (content) {
            case "if" -> new AktkToken(IF);
            case "while" -> new AktkToken(WHILE);
            case "var" -> new AktkToken(VAR);
            default -> new AktkToken(VARIABLE, content);
        };
        token.setPosition(initPos, content.length());
        return token;
    }

    private AktkToken readString() {
        assert peek() == '"';
        int initPos = this.pos;

        StringBuilder sb = new StringBuilder();

        consume();
        while (peek() != '"') {
            if (peek() == '\\') {
                consume();
                switch (peek()) {
                    case 'n' -> sb.append('\n');
                    case 't' -> sb.append('\t');
                    case '"' -> sb.append('\"');
                    default -> throw new RuntimeException("Unknown character escape");
                }
            } else {
                sb.append(peek());
            }
            consume();
        }
        consume();

        return new AktkToken(STRING, sb.toString(), initPos, this.pos - initPos);
    }

    private AktkToken readInteger() {
        assert Character.isDigit(peek());

        int initPos = this.pos;
        int result = 0;
        while (Character.isDigit(peek())) {
            result = 10 * result + Character.getNumericValue(peek());
            consume();
        }

        AktkToken token = new AktkToken(INTEGER, result);
        token.setPosition(initPos, pos - initPos);
        return token;
    }
}
