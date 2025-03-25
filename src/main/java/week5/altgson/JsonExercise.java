package week5.altgson;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

// https://courses.cs.ut.ee/t/akt/Main/Gson
public class JsonExercise {

    // Tagastage raamatu ISBN kood
    public static String getTitleIsbn(String title, String jsonString) {
        throw new UnsupportedOperationException();
    }

    // Tagastage hulk kõikidest autoritest, keda on kirjastanud antud kirjastus
    public static Set<String> getAuthorsByPublisher(String publisher, String jsonString) {
        throw new UnsupportedOperationException();
    }

    // Tagasta raamat objektina, mis implementeerib Publication liidest.
    public static Publication getAsPublication(String title, String jsonString) {
        throw new UnsupportedOperationException();
    }

    // Filtreerige välja kõik antud žanri raamatuid, s.t. tagastada tuleks JSON sõne
    public static String filterByGenre(String genre, String jsonString) {
        throw new UnsupportedOperationException();
    }

    // Milline autor on kirjutanud kõige rohkem antud žanri raamatu lehekülgi kokku
    public static String mostPagesInGenre(String genre, String input) {
        throw new UnsupportedOperationException();
    }


    // Main meetod testmiseks -- päris testid on failis test/week5/altgson/JsonExerciseTest.java
    public static void main(String[] args) throws IOException {
        String jsonString = Files.readString(Paths.get("inputs/books.json"));
        System.out.println(getTitleIsbn("Toomas Nipernaadi", jsonString));
        System.out.println(getAuthorsByPublisher("Sinisukk", jsonString));
        System.out.println(getAsPublication("Kevade", jsonString));
        System.out.println(filterByGenre("romaan", jsonString));
        System.out.println(mostPagesInGenre("romaan", jsonString));
    }
}
