package week3.danger;

import week3.buildingblocks.Transition;

import java.util.HashSet;
import java.util.Set;
import java.util.function.UnaryOperator;

import static com.google.common.collect.Sets.union;
import static java.util.Collections.emptySet;
import static week3.buildingblocks.TransitionUtils.compose;


/**
 * Siin on paar rida koodi, mis implementeerivad õpikus kirjeldatud mõttekäiku.
 * Selleks peab lugema peatükk 1.5.1, muidu on väga raske siit midagi aru saada.
 *
 */
public class Fixpoints {

    /**
     * Püsipunkti leidmiseks rakenendadme järjest funktsiooni f argumendile x.
     * Iteratsioon lõpeb siis ja ainult siis, kui on leitud selline y, et f(y) == y.
     */
    public static <T> T fixpoint(UnaryOperator<T> f, T x) {
        while (true) {
            T next = f.apply(x);
            if (x.equals(next)) return x;
            x = next;
        }
    }

    /**
     * Nüüd saab ülaloleva üldise funktsiooni abil defineerida meetodd  leastFixpoint,
     * mis lahendab õpiku hulgavõrrandid alustades iteratsiooni tühjast hulgast.
     */
    public static <T> Set<T> leastFixpoint(UnaryOperator<Set<T>> f) {
        return fixpoint(f, emptySet());
    }

    /**
     * Siis defineerime relatsiooni R sulund. Õpikus defineeritaakse epsilon-sulundi
     * jaoks funktsioon F_M. Relatsiooni R korral on see järgmine:
     *   F_M(X) = M ⋃ { t | s ∈ X ja (s,t) ∈ R } = M ⋃ R(X)
     * Esitame relatsiooni R sellisel kujul, et R(X) tagastab kõik elemendid t,
     * mille kohta leidub s ∈ X, et s ja t on seoses R. Hulga M sulund
     * relatsiooni f suhtes (ehk tema f-sulund) on lihtsalt funktsiooni
     * F_m(x) = m ⋃ f(x) vähim püsipunkt.
     */
    public static <T> Set<T> closure(UnaryOperator<Set<T>> f, Set<T> m) {
        throw new UnsupportedOperationException();
    }

    // Ja nüüd me saame transitiivset sulundit lihtsalt arvutada, kui
    // funktsiooni f_r(x) = x∘r sulund, alustades relatsioonist r.
    public static Set<Transition> transitiveClosure(Set<Transition> rel) {
        return closure(x -> compose(x, rel), rel);
    }

    public static void main(String[] args) {
        Set<Transition> rel0 = new HashSet<>();
        rel0.add(new Transition(0, "a", 1));
        rel0.add(new Transition(1, "b", 2));
        rel0.add(new Transition(1, "c", 3));
        rel0.add(new Transition(3, "d", 4));

        Set<Transition> rel1 = compose(rel0, rel0); System.out.println(rel1);
        Set<Transition> rel2 = compose(rel1, rel0); System.out.println(rel2);
        Set<Transition> rel3 = compose(rel2, rel0); System.out.println(rel3);

        System.out.println(transitiveClosure(rel0));
        //[(0,a,1), (1,b,2), (1,c,3), (3,d,4), (0,ab,2), (0,ac,3), (1,cd,4), (0,acd,4)]
    }

}
