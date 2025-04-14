package toylangs.imp;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaProgramWriter;
import cma.CMaStack;
import toylangs.imp.ast.*;

import java.io.IOException;
import java.nio.file.Paths;

import static toylangs.imp.ast.ImpNode.*;

public class ImpCompiler {

    public static CMaProgram compile(ImpProg prog) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }

    public static void main(String[] args) throws IOException {
        ImpProg prog = prog(
                var('x'),

                assign('x', num(5)),
                assign('y', add(var('x'), num(1))),
                assign('x', add(var('y'), num(1)))
        );

        // väärtustame otse
        System.out.printf("eval: %d%n", ImpEvaluator.eval(prog));

        // kompileeri avaldist arvutav CMa programm
        CMaProgram program = compile(prog);

        // kirjuta programm faili, mida saab Vam-iga vaadata
        CMaStack initialStack = new CMaStack();
        program.toFile(Paths.get("cmas", "imp.cma"), initialStack);

        // interpreteeri CMa programm
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);
        System.out.printf("compiled: %d%n", finalStack.peek());
        System.out.printf("finalStack: %s%n", finalStack);
    }
}
