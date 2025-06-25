package eksam3.ast;

import toylangs.AbstractNode;

import java.util.Arrays;
import java.util.List;

public abstract class SimpNode extends AbstractNode {

    // Võimaldavad natuke mugavamalt luua neid objekte:
    public static SimpNum num(int value) {
        return new SimpNum(value);
    }

    public static SimpAdd add(SimpNode left, SimpNode right) {
        return new SimpAdd(left, right);
    }

    public static SimpMul mul(SimpNode left, SimpNode right) {
        return new SimpMul(left, right);
    }

    public static SimpMem mem(int loc) {
        return new SimpMem(loc);
    }

    public static SimpProg prog(List<SimpNode> exps) {
        return new SimpProg(exps);
    }

    public static SimpProg prog(SimpNode... exps) {
        return new SimpProg(Arrays.asList(exps));
    }

    // Visitor ja listener implementatsiooniks:
    public abstract <T> T accept(SimpAstVisitor<T> visitor);
}
