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
        return new AktkToken(EOF, pos, 0);
    }
}
