package toylangs.pullet;

import cma.*;
import toylangs.pullet.ast.*;

import java.util.Arrays;
import java.util.List;

import static toylangs.pullet.ast.PulletNode.*;

public class PulletCompiler {

    private final static List<String> VARS = Arrays.asList("x", "y", "z");

    // Koodi genereerimiseks eeldame, et let'id ja summad ei esine alamavaldistena!
    // Meil on lisaks mõned globaalsed muutujad x, y, z, mis asuvad pesades 0, 1 ja 2.
    public static CMaProgram compile(PulletNode expr) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }


    public static void main(String[] args) {
        PulletNode avaldis = let("a",num(666),let("b", diff(var("a"),num(1)),num(3)));
        System.out.println(avaldis);
        System.out.println(PulletEvaluator.eval(avaldis));
        System.out.println(CMaInterpreter.run(compile(avaldis), new CMaStack(0, 0, 0)));
    }
}
