package toylangs.pullet;

import cma.*;
import toylangs.pullet.ast.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static cma.instruction.CMaLabelInstruction.Code.JUMP;
import static cma.instruction.CMaLabelInstruction.Code.JUMPZ;
import static toylangs.pullet.ast.PulletNode.*;

public class PulletCompiler {

    private final static List<String> VARS = Arrays.asList("x", "y", "z");

    // Koodi genereerimiseks eeldame, et let'id ja summad ei esine alamavaldistena!
    // Meil on lisaks mõned globaalsed muutujad x, y, z, mis asuvad pesades 0, 1 ja 2.
    public static CMaProgram compile(PulletNode expr) {
        CMaProgramWriter pw = new CMaProgramWriter();

        Map<String, Integer> env = new HashMap<>();
        for (String var : VARS) env.put(var, VARS.indexOf(var));

        new PulletAstVisitor<Void>() {
            int SP = env.size();

            @Override
            protected Void visit(PulletNum num) {
                pw.visit(LOADC, num.getNum());
                return null;
            }

            @Override
            protected Void visit(PulletVar var) {
                pw.visit(LOADA, env.get(var.getName()));
                return null;
            }

            @Override
            protected Void visit(PulletBinding binding) {
                String nimi = binding.getName();
                visit(binding.getValue());
                env.put(nimi, SP++);
                visit(binding.getBody());
                return null;
            }

            @Override
            protected Void visit(PulletSum sum) {
                // avaldis kujul idx = lo to hi in ...idx...

                // Alustame idx = lo
                String nimi = sum.getName();
                visit(sum.getLo());
                int idx = SP++;
                env.put(nimi, idx);

                visit(sum.getHi());
                int high = SP++;
                CMaLabel _while = new CMaLabel();
                CMaLabel _exit = new CMaLabel();

                // Tulemuse salvestamiseks:
                pw.visit(LOADC, 0);

                // while (idx <= high)
                pw.visit(_while);
                pw.visit(LOADA, idx);
                pw.visit(LOADA, high);
                pw.visit(LEQ);
                pw.visit(JUMPZ, _exit);

                // result += keha;
                visit(sum.getBody());
                pw.visit(ADD);

                // idx++;
                pw.visit(LOADA, idx);
                pw.visit(LOADC, 1);
                pw.visit(ADD);
                pw.visit(STOREA, idx);
                pw.visit(POP);

                pw.visit(JUMP, _while);

                pw.visit(_exit);

                return null;
            }

            @Override
            protected Void visit(PulletDiff diff) {
                visit(diff.getLeft());
                visit(diff.getRight());
                pw.visit(SUB);
                return null;
            }
        }.visit(expr);

        return pw.toProgram();
    }


    public static void main(String[] args) {
        PulletNode avaldis = let("a",num(666),let("b", diff(var("a"),num(1)),num(3)));
        System.out.println(avaldis);
        System.out.println(PulletEvaluator.eval(avaldis));
        System.out.println(CMaInterpreter.run(compile(avaldis), new CMaStack(0, 0, 0)));
    }
}
