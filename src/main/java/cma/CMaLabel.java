package cma;

public class CMaLabel {

    private static int nextId = 1;

    private final int id = nextId++;

    @Override
    public String toString() {
        return "L" + id;
    }
}
