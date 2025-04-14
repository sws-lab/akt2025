package toylangs.pullet;

import toylangs.pullet.ast.*;

import static toylangs.pullet.ast.PulletNode.*;

public class PulletMaster {

    public static boolean isLive(String name, PulletNode expr) {
        throw new UnsupportedOperationException();
    }

    public static PulletNode optimize(PulletNode expr) {
        throw new UnsupportedOperationException();
    }


    public static void main(String[] args) {
        PulletNode avaldis = let("a",num(666),let("b", diff(var("a"),num(1)),var("a")));
        System.out.println(avaldis);
        System.out.println(PulletMaster.optimize(avaldis));
    }
}
