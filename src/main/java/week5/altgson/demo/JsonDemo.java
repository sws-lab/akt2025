package week5.altgson.demo;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import week5.altgson.demo.Student.StudentAnnotated;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

// https://courses.cs.ut.ee/t/akt/Main/Gson

public class JsonDemo {

    // Serialiseerimine
    public static void demo1a() {
        Gson gson = new Gson();
        // Loome uue Student objekti
        Student student = new Student("Andres", "Paas", 24);
        // Teisendame objekti JSON sõneks
        String json = gson.toJson(student);
        System.out.println(json);
    }

    // Serialiseerimine
    public static void demo1b() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE) // Muudab väljade nimede esitähed suureks
                .setPrettyPrinting() // Prindib väljad eraldi ridadel
                .create();
        // Loome uue Student objekti
        Student student = new Student("Andres", "Paas", 24);
        // Teisendame objekti JSON sõneks
        String json = gson.toJson(student);
        System.out.println(json);
    }


    // Deserialiseerimine failist
    public static void demo2a() throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader("inputs/input.json"));
        Student student = gson.fromJson(reader, Student.class);
        System.out.println(student.toString());
    }

    // Deserialiseerimine sõnest
    public static void demo2b() {
        Gson gson = new Gson();
        // Paneme tähele, et "courses" on väli mida meie StudentAnnotated klassis ei leidu
        String jsonString = """
                {
                  "nimi": "Pipi",
                  "perenimi": "Pikksukk",
                  "vanus": 20,
                  "courses": ["Sissejuhatus Erialasse", "Programmeerimine", "Matemaatiline maailmapilt"]
                }""";
        StudentAnnotated student = gson.fromJson(jsonString, StudentAnnotated.class);
        System.out.println(student.toString());
    }


    public static void demo3() throws IOException {
        String jsonString = Files.readString(Paths.get("inputs/input.json"));
        JsonObject jsonTree = JsonParser.parseString(jsonString).getAsJsonObject();
        String firstname = jsonTree.get("firstname").getAsString();
        System.out.println(firstname);

        // Kuna kursused on massiivis, peame neid veidi teistmoodi käsitlema
        JsonArray courses = jsonTree.getAsJsonArray("courses");
        for (Object course : courses) System.out.println(course.toString());

        // Kui tahame saada objekte kätte, mis võib olla kasulik, kui on list oma klassi asjadest:
        Gson gson = new Gson();
        String[] coursesArray = gson.fromJson(jsonTree.get("courses"), String[].class);
        System.out.println(Arrays.toString(coursesArray));

        // Kui tahta sõnede Listiks, siis oli vist keerulisem, ütleme kõigepealt mis list ta on:
        Type type = TypeToken.getParameterized(List.class, String.class).getType();
        List<String> coursesList = gson.fromJson(jsonTree.get("courses"), type);
        System.out.println(coursesList);

        // Kõige lihtsam on aga luua oma kirjeklass, vt definitsioon allpool,
        // siis saame otse algsest sisendist lugeda:
        School school = gson.fromJson(jsonString, School.class);
        System.out.println(school.courses());
    }

    // Siin on kirje definitsioon, mis laseb lihtsamini lugeda kursuste listi
    record School(List<String> courses) {}

    public static void main(String[] args) throws IOException {
        demo1a();
        demo1b();
        demo2a();
        demo2b();
        demo3();
    }
}
