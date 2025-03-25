package proovieksam.ast;

import proovieksam.ast.aatomid.EstologLiteraal;
import proovieksam.ast.aatomid.EstologMuutuja;
import proovieksam.ast.operaatorid.EstologJa;
import proovieksam.ast.operaatorid.EstologVoi;
import proovieksam.ast.operaatorid.EstologVordus;
import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public abstract class EstologNode extends AbstractNode {

    public static EstologMuutuja var(String nimi) {
        return new EstologMuutuja(nimi);
    }

    public static EstologLiteraal lit(boolean value) {
        return new EstologLiteraal(value);
    }

    public static EstologJa ja(EstologNode left, EstologNode right) {
        return new EstologJa(left, right);
    }

    public static EstologVoi voi(EstologNode left, EstologNode right) {
        return new EstologVoi(left, right);
    }

    public static EstologVordus vordus(EstologNode left, EstologNode right) {
        return new EstologVordus(left, right);
    }

    public static EstologKui kui(EstologNode kui, EstologNode siis, EstologNode muidu) {
        return new EstologKui(kui, siis, muidu);
    }

    public static EstologKui kui(EstologNode kui, EstologNode siis) {
        return new EstologKui(kui, siis);
    }

    public static EstologDef def(String nimi, EstologNode avaldis) {
        return new EstologDef(nimi, avaldis);
    }

    public static EstologProg prog(EstologNode avaldis, List<EstologDef> defs) {
        return new EstologProg(avaldis, defs);
    }

    public static EstologProg prog(EstologNode avaldis, EstologDef... defs) {
        return new EstologProg(avaldis, Arrays.asList(defs));
    }

    public abstract <T> T accept(EstologAstVisitor<T> visitor);
}
