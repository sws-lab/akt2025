package week1.soojendus;

/**
 * Klass on mõeldud kahendpuu tipu esitamiseks. Tüübiparameeter T määrab, millist
 * tüüpi väärtust saab tipus hoida. Väärtuse küsimiseks on meetod getValue.
 */
public class Node<T> {

    public Node(T value, Node<T> leftChild, Node<T> rightChild) {
        // Mida võiks siin teha? Kas midagi on äkki kuskil puudu?
    }

    public T getValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * Tagastab vasaku alampuu. Selle puudumisel tagastab null-i.
     */
    public Node<T> getLeftChild() {
        throw new UnsupportedOperationException();
    }

    public Node<T> getRightChild() {
        throw new UnsupportedOperationException();
    }

    /**
     * Tagastab true tippude korral, millel pole ühtegi alampuud.
     */
    public boolean isLeaf() {
        throw new UnsupportedOperationException();
    }


    /**
     * Meetod peab tagastama true või false vastavalt sellele, kas antud (alam)puus
     * leidub näidatud väärtus. Võrdlemine toimub meetodi equals alusel.
     */
    public boolean contains(T value) {
        throw new UnsupportedOperationException();
    }


    /**
     * toString peab tagastama puu suluesituse.
     *
     * Formaat:
     * Puu iga tipp on esitatud ümarsulgudes antud kolmikuna, kus esimene
     * element on tipu väärtus, teine element on vasak alluv ning kolmas on parem alluv.
     * Kui alluv puudub, siis on sõnes vastaval kohal allkriips.
     *
     * Komponentide vahel peab olema koma ja üks tühik.
     *
     * Näited:
     * kui t on ühetipuline puu tüübiga Node<Integer>,
     * mille tipu väärtus on 3, siis t.toString() peab tagastama
     * "(3, _, _)"
     *
     *
     * Kui t on selline puu:
     *      1
     *     / \
     *    /   \
     *   2    -44
     *    \
     *     3
     *
     * siis t.toString() peab tagastama
     *
     * "(1, (2, _, (3, _, _)), (-44, _, _))"
     */
    @Override
    public String toString() {
        return super.toString(); // Asenda oma koodiga
    }
}
