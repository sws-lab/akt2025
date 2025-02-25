package week3.buildingblocks;

import java.util.HashSet;
import java.util.Set;

public class TransitionUtils {

    public static Set<Transition> compose(Set<Transition> rel1, Set<Transition> rel2) {
        Set<Transition> result = new HashSet<>();
        for (Transition t1 : rel1) {
            for (Transition t2 : rel2) {
                if (t1.getTarget() == t2.getSource()) {
                    String label = t1.getLabel() + t2.getLabel();
                    result.add(new Transition(t1.getSource(), label, t2.getTarget()));
                }
            }
        }
        return result;
    }

    // Arvutame nüüd transitiivse sulundi relatsiooni astendamise teel:
    public static Set<Transition> transitiveClosure(Set<Transition> rel) {
        Set<Transition> closure = new HashSet<>();
        Set<Transition> relPowers = new HashSet<>(rel);
        // Peab lisama sulundite hulka kõik uued üleminekud, kuni asi enam ei muutu.
        // Vaja arvutada R^i = compose(R^{i-1},R) ehk tsüklis astendamist korrata:
        while (closure.addAll(relPowers)) relPowers = compose(relPowers, rel);
        return closure;
    }

    public static void main(String[] args) {
        Set<Transition> rel = new HashSet<>();
        rel.add(new Transition(0, "a", 1));
        rel.add(new Transition(1, "b", 2));
        rel.add(new Transition(1, "c", 3));
        System.out.println(transitiveClosure(rel)); // [(0,a,1), (0,ab,2), (0,ac,3), (1,b,2), (1,c,3)]
    }
}
