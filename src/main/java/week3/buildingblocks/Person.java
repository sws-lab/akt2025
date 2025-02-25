package week3.buildingblocks;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Person {

    private final String name;
    private final int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public static Map<String, Integer> makeAgeMap(Set<Person> people) {
        Map<String, Integer> map = new HashMap<>();
        // Lisada kõik inimesed siia map'ile, et nende nimele vastaks nende vanus.
        return map;
    }

    public static Map<String, Set<Integer>> makeAgeMultimap(Set<Person> people) {
        Map<String, Set<Integer>> map = new HashMap<>();
        // Sama asi on nüüd vaja teha. Kui on aga mitut sama nimega inimest erinevate vanustega,
        // siis tahame koguda kõik vastavad vanused hulka, näiteks sisendi
        //   { (Anna, 13), (Jüri, 10), (Anna, 15) }
        // ootame tulemusena
        //   { Anna -> {13,15}, Jüri -> {10} }
        // Peamine tüütus on siin see, et neid hulkasid peab ise looma!
        return map;
    }

    public static Multimap<String, Integer> makeAgeGuavaMultimap(Set<Person> people) {
        Multimap<String, Integer> map = HashMultimap.create();
        // Mmm vihje: saad copy-paste'iga lahendada!
        return map;
    }


    public static void main(String[] args) {

        // Esimeses klassis on kõik hästi...
        Set<Person> esimeneKlass = new HashSet<>();
        esimeneKlass.add(new Person("Mari", 9));
        esimeneKlass.add(new Person("Juhan", 8));
        System.out.println(makeAgeMap(esimeneKlass));

        // Aga siin on kaks sama nimega inimest ja tahaks, et jääks mõlemad vanused meelde:
        Set<Person> teineKlass = new HashSet<>();
        teineKlass.add(new Person("Mari", 18));
        teineKlass.add(new Person("Juhan", 12));
        teineKlass.add(new Person("Mari", 19));

        System.out.println(makeAgeMap(teineKlass));
        System.out.println(makeAgeMultimap(teineKlass));
        System.out.println(makeAgeGuavaMultimap(teineKlass));

    }
}
