package week1.soojendus;

public class NodeUtils {

    /**
     * Meetod peab tagastama täisarvu, mis näitab mitu korda esineb antud (alam)puus
     * näidatud väärtus. NB! Võrdlemiseks kasutada meetodit equals!
     */
    public static <T> int count(Node<T> tree, T value) {
        throw new UnsupportedOperationException();
    }


    /**
     * NB! See on raskem ülesanne. Enne selle üritamist lahenda ja esita teised ülesanded ära!
     *
     *
     * See meetod peab võtma suluesituses sõnena antud puu ja tagastama vastava
     * puu andmestruktuuri. Tippudes oleva väärtuse tüüp on Integer.
     *
     * Sõne formaat on sama, nagu kirjeldatud Node.toString juures.
     *
     * Näited
     *
     * Kui käivitada
     *
     *      puu = parseIntegerTree("(1, (2, _, (3, _, _)), (3, _, _))");
     *
     * siis puu.getLeft().getRight().getValue() peab tagastama 3
     * ja   puu.getRight().isLeaf() peab tagastama true
     * ja   NodeUtils.count(puu, 3) peab tagastama 2
     *
     */
    public static Node<Integer> parseIntegerTree(String s) {
        throw new UnsupportedOperationException();
    }
}


