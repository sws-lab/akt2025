package week1.soojendus.animals;

public abstract class Animal {
    private static int nextId = 1;

    private final int id = nextId++;

    public void makeNoise() {
        System.out.println("Loom #" + id + ": " + this);
    }
}
