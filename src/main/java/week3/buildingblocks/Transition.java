package week3.buildingblocks;

import java.util.*;

public class Transition {

    private final int source;
    private final String label;
    private final int target;

    public Transition(int source, String label, int target) {
        this.source = source;
        this.label = label;
        this.target = target;
    }

    public int getSource() {
        return source;
    }

    public String getLabel() {
        return label;
    }

    public int getTarget() {
        return target;
    }

    public static Map<Integer, Map<String, Integer>> makeTransitionMap(Set<Transition> transitions) {
        Map<Integer, Map<String, Integer>> map = new HashMap<>();
        // Siin on peamine mure, et mapi sees on omakorda vaja neid mapikesed luua!
        return map;
    }

    public static void main(String[] args) {
        Set<Transition> transitions = new HashSet<>();
        transitions.add(new Transition(1, "a", 2));
        transitions.add(new Transition(1, "b", 3));
        transitions.add(new Transition(2, "c", 3));
        System.out.println(makeTransitionMap(transitions)); // {1={a=2, b=3}, 2={c=3}}
    }

    @Override
    public String toString() {
        return "(%d,%s,%d)".formatted(source, label, target);
    }


    // IntelliJ autogenereeritud equals & Hashcode.
    // (Ainult vajalik TransitionUtils jaoks.)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition transition = (Transition) o;
        if (source != transition.source) return false;
        if (target != transition.target) return false;
        return Objects.equals(label, transition.label);
    }

    @Override
    public int hashCode() {
        int result = source;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + target;
        return result;
    }

}
