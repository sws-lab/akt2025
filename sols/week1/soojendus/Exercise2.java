package week1.soojendus;

import week1.soojendus.animals.Animal;
import week1.soojendus.animals.Kass;
import week1.soojendus.animals.Koer;

import java.util.Arrays;
import java.util.List;

// Väljund peaks olema:
//    Loom #1: Mjau!
//    Loom #2: Auh-auh!
//    Loom #1: Mjau!
//    Loom #3: Mjau!
//    Loom #2: Auh-auh!
public class Exercise2 {
    public static void main(String[] args) {
        Kass a1 = new Kass();
        Koer a2 = new Koer();
        Kass a3 = new Kass();

        List<Animal> animals = Arrays.asList(a1, a2, a1, a3, a2);
        for (Animal animal : animals) animal.makeNoise();
    }
}
