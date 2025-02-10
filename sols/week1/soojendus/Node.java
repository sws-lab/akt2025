package week1.soojendus;

/**
 * Klass on mõeldud kahendpuu tipu esitamiseks. Tüübiparameeter T määrab, millist
 * tüüpi väärtust saab tipus hoida. Väärtuse küsimiseks on meetod getValue.
 */
public class Node<T> {
    private final Node<T> left;
    private final Node<T> right;
    private final T value;

    public Node(T value, Node<T> leftChild, Node<T> rightChild) {
        this.value = value;
        this.left = leftChild;
        this.right = rightChild;
    }

    public T getValue() {
        return value;
    }

    /**
     * Tagastab vasaku alampuu. Selle puudumisel tagastab null-i.
     */
    public Node<T> getLeftChild() {
        return left;
    }

    public Node<T> getRightChild() {
        return right;
    }

    /**
     * Tagastab true tippude korral, millel pole ühtegi alampuud.
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }


    /**
     * Meetod peab tagastama true või false vastavalt sellele, kas antud (alam)puus
     * leidub näidatud väärtus. Võrdlemine toimub meetodi equals alusel.
     */
    public boolean contains(T value) {
        return this.value.equals(value) ||
                left != null && left.contains(value) ||
                right != null && right.contains(value);
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
        return "(%s, %s, %s)".formatted(
                value,
                left == null ? "_" : left,
                right == null ? "_" : right);
    }
}
