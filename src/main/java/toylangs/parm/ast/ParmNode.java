package toylangs.parm.ast;

import toylangs.AbstractNode;

public abstract class ParmNode extends AbstractNode {

    // Literaalid ja muutuja kasutus
    public static ParmLit lit(int value) { return new ParmLit(value); }
    public static ParmVar var(String name) { return new ParmVar(name); }

    // Liitmine
    public static ParmPlus plus(ParmNode left, ParmNode right) {
        return new ParmPlus(left, right);
    }

    // Omistus
    public static ParmUpdate up(String variable, ParmNode value) {
        return new ParmUpdate(variable, value);
    }

    // Komponeerimine järjestikune ja paralleelne
    public static ParmCompose seq(ParmNode fst, ParmNode snd) {
        return new ParmCompose(fst, snd, false);
    }
    public static ParmCompose par(ParmNode fst, ParmNode snd) {
        return new ParmCompose(fst, snd, true);
    }

    public abstract <T> T accept(ParmAstVisitor<T> visitor);
}
