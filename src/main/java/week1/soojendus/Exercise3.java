package week1.soojendus;

import java.util.*;

public class Exercise3 {

    /**
     * Defineeri meetod eval, mis väärtustab etteantud avaldise.
     * @param str on plussidega eraldatud arvude jada, näiteks "5 + 35+  10".
     * @return arvude summa, antud näide puhul 50.
     */
    public static int eval(String str) {
        throw new UnsupportedOperationException();
    }

    /**
     * Tuletame lihtsalt meelde Java List ja Map andmestruktuurid!
     * Selle ülesanne puhul võiks tegelikult tüüpide ja main meetodi põhjal aru saada, mida tegema peaks...
     *
     * @param list sõnedest, kus on vaheldumisi nimi ja arv (sõne kujul). Võib eeldada, et pikkus on paarisarv.
     * @return listile vastav map nimedest arvudesse.
     */
    public static Map<String, Integer> createMap(List<String> list) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        System.out.println(eval("2+2"));
        Map<String, Integer> ageMap = createMap(Arrays.asList("Carmen", "17", "Jürgen", "44", "Tarmo", "10", "Mari", "83"));
        System.out.println(ageMap.get("Carmen")); // vastus: 17
        System.out.println(ageMap.get("Tarmo"));  // vastu: 10
    }
}
