package toylangs.pullet.ast;

import toylangs.AbstractNode;

public abstract class PulletNode extends AbstractNode {

    public static PulletNode num(int n) {
        return new PulletNum(n);
    }

    public static PulletNode var(String name) {
        return new PulletVar(name);
    }

    public static PulletNode diff(PulletNode left, PulletNode right) {
        return new PulletDiff(left, right);
    }

    public static PulletNode let(String name, PulletNode value, PulletNode body) {
        return new PulletBinding(name, value, body);
    }

    public static PulletNode sum(String name, PulletNode lo, PulletNode hi, PulletNode body) {
        return new PulletSum(name, lo, hi, body);
    }

    public abstract <T> T accept(PulletAstVisitor<T> visitor);
}
