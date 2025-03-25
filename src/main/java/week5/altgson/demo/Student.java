package week5.altgson.demo;

import com.google.gson.annotations.SerializedName;

public class Student {
    String firstname;
    String surname;
    int age;

    public Student(String firstname, String surname, int age) {
        this.firstname = firstname;
        this.surname = surname;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{firstname='%s', surname='%s', age=%d}".formatted(firstname, surname, age);
    }


    // Siin on erinevad variandid annotatsiooni, kirjed, annotatsioonidega kirjed :)

    public static class StudentAnnotated {
        @SerializedName(value = "firstname", alternate = {"name", "nimi", "eesnimi"})
        String firstname;
        @SerializedName(value = "surname", alternate = {"familyname", "perekonnanimi", "perenimi"})
        String surname;
        @SerializedName(value = "age", alternate = "vanus")
        int age;

        public StudentAnnotated(String firstname, String surname, int age) {
            this.firstname = firstname;
            this.surname = surname;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Student{firstname='%s', surname='%s', age=%d}".formatted(firstname, surname, age);
        }
    }

    public record StudentRecord (String firstname, String surname, int age) {}

    public record StudentRecordAnnotated (
            @SerializedName(value = "firstname", alternate = {"name", "nimi", "eesnimi"})
            String firstname,
            @SerializedName(value = "surname", alternate = {"familyname", "perekonnanimi", "perenimi"})
            String surname,
            @SerializedName(value = "age", alternate = "vanus")
            int age
    ) { }
}
