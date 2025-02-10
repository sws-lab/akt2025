package week1.soojendus;

public class NodeUtils {

    /**
     * Meetod peab tagastama täisarvu, mis näitab mitu korda esineb antud (alam)puus
     * näidatud väärtus. NB! Võrdlemiseks kasutada meetodit equals!
     */
    public static <T> int count(Node<T> tree, T value) {
        int res = tree.getValue().equals(value) ? 1 : 0;

        if (tree.getLeftChild() != null) {
            res += count(tree.getLeftChild(), value);
        }

        if (tree.getRightChild() != null) {
            res += count(tree.getRightChild(), value);
        }

        return res;
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
        return new IntegerNodeParser(s).parse();
    }

    private static class IntegerNodeParser {
        private final String s;
        private int pos;

        private IntegerNodeParser(String s) {
            this.s = s;
            this.pos = 0;
        }

        public Node<Integer> parse() {
            if (s.charAt(pos) == '_') {
                pos++;
                return null;
            } else {
                pos++; // '('
                int value = parseInt();
                pos++; // ','
                pos++; // ' '
                Node<Integer> left = parse();
                pos++; // ','
                pos++; // ' '
                Node<Integer> right = parse();
                pos++; // ')'
                return new Node<>(value, left, right);
            }
        }

        private int parseInt() {
            int sign = 1;
            if (s.charAt(pos) == '-') {
                sign = -1;
                pos++;
            }

            int value = 0;
            while (s.charAt(pos) != ',') {
                value = 10 * value + Character.getNumericValue(s.charAt(pos));
                pos++;
            }

            return sign * value;
        }

        @SuppressWarnings("unused")
        private int parseIntJava() {
            int oldPos = pos;
            while (s.charAt(pos) != ',') pos++;
            return Integer.parseInt(s.substring(oldPos, pos));
        }
    }
}


