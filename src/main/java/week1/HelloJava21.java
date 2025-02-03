package week1;

import java.util.List;

public class HelloJava21 {

    public static void main(String[] args) {
        List<Object> objs = List.of("Tere", false, "Java", 21, "hüüumärk"); // Java 9
        System.out.println(objs);

        int answer = objs.stream() // Java 8
                .mapToInt(HelloJava21::convert)
                .sum();
        System.out.println(answer);
    }

    private static int convert(Object obj) {
        return switch (obj) { // Java 16
            case String str -> // Java 21
                switch (str) { // Java 16
                    case "Tere" -> -8;
                    case "Java" -> 21;
                    default -> str.length();
                };
            case Integer i -> i; // Java 21
            default -> 0;
        };
    }
}
