package week5.altgson;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static week5.altgson.JsonExercise.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JsonExerciseTest {

    String testinput;

    @Before
    public void setUp() throws IOException {
        testinput = Files.readString(Paths.get("inputs/books.json"));
    }

    @Test
    public void test01_isbn() {
        assertEquals("9789949538720", getTitleIsbn("Toomas Nipernaadi", testinput));
        assertEquals("9789949144969", getTitleIsbn("Pipi Pikksukk", testinput));
    }

    @Test
    public void test02_authors() {
        assertEquals(Set.of("Oskar Luts", "Astrid Lindgren"), getAuthorsByPublisher("Sinisukk", testinput));
        assertEquals(Set.of("Aristarch Sinkel"), getAuthorsByPublisher("Revelex", testinput));
        assertEquals(Set.of("Ilmar Tomusk", "Eno Raud", "Joonas Sildre"), getAuthorsByPublisher("Tammeraamat", testinput));
    }

    @Test
    public void test03_publication() {
        // Tagasta raamat "Kevade" objektina mis implementeerib Literature interface.
        Publication result = getAsPublication("Kevade", testinput);
        assertEquals("Kevade", result.getTitle());
        assertEquals("Oskar Luts", result.getAuthors().getFirst());
        assertEquals(19.5, result.getPrice(), 0.1);
    }

    @Test
    public void test04_filter() {
        assertEquals(68539598416856L, isbnChecksum(filterByGenre("lasteraamat", testinput)));
        assertEquals(88110141241669L, isbnChecksum(filterByGenre("romaan", testinput)));
    }

    private long isbnChecksum(String jsonString) {
        JsonArray books = JsonParser.parseString(jsonString).getAsJsonObject().get("books").getAsJsonArray();
        return books.asList().stream().mapToLong(e -> e.getAsJsonObject().get("ISBN").getAsLong()).sum();
    }

    @Test
    public void test05_mostpages() {
        // Milline autor on kirjutanud kõige rohkem antud žanri raamatu lehekülgi kokku
        assertEquals("Oskar Luts", mostPagesInGenre("romaan", testinput));
        assertEquals("Eno Raud", mostPagesInGenre("lasteraamat", testinput));
    }
}
