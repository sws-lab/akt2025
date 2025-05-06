package toylangs.imp;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaProgramWriter;
import cma.CMaStack;
import toylangs.imp.ast.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static toylangs.imp.ast.ImpNode.*;

public class ImpCompiler {

    public static CMaProgram compile(ImpProg prog) {
        CMaProgramWriter pw = new CMaProgramWriter();

        new ImpAstVisitor<Void>() {
            private final List<Character> variables = new ArrayList<>();

            @Override
            protected Void visit(ImpNum num) {
                pw.visit(LOADC, num.getValue());
                return null;
            }

            @Override
            protected Void visit(ImpNeg neg) {
                visit(neg.getExpr());
                pw.visit(NEG);
                return null;
            }

            @Override
            protected Void visit(ImpAdd add) {
                visit(add.getLeft());
                visit(add.getRight());
                pw.visit(ADD);
                return null;
            }

            @Override
            protected Void visit(ImpDiv div) {
                visit(div.getNumerator());
                visit(div.getDenominator());
                pw.visit(DIV);
                return null;
            }

            @Override
            protected Void visit(ImpVar var) {
                int variableIndex = variables.indexOf(var.getName());
                if (variableIndex < 0) throw new NoSuchElementException("Undefined variable " + var.getName());
                pw.visit(LOADA, variableIndex);
                return null;
            }

            @Override
            protected Void visit(ImpAssign assign) {
                visit(assign.getExpr());

                int variableIndex = variables.indexOf(assign.getName());
                if (variableIndex >= 0) {
                    pw.visit(STOREA, variableIndex);
                    pw.visit(POP);
                }
                else {
                    variables.add(assign.getName());
                }
                return null;
            }

            @Override
            protected Void visit(ImpProg prog) {
                for (ImpAssign assign : prog.getAssigns()) {
                    visit(assign);
                }
                visit(prog.getExpr());
                return null;
            }
        }.visit(prog);

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
