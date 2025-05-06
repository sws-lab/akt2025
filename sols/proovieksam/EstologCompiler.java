package proovieksam;

import cma.*;
import proovieksam.ast.EstologAstVisitor;
import proovieksam.ast.EstologDef;
import proovieksam.ast.EstologKui;
import proovieksam.ast.EstologProg;
import proovieksam.ast.aatomid.EstologLiteraal;
import proovieksam.ast.aatomid.EstologMuutuja;
import proovieksam.ast.operaatorid.EstologJa;
import proovieksam.ast.operaatorid.EstologVoi;
import proovieksam.ast.operaatorid.EstologVordus;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static cma.instruction.CMaLabelInstruction.Code.JUMP;
import static cma.instruction.CMaLabelInstruction.Code.JUMPZ;
import static proovieksam.ast.EstologNode.*;

public class EstologCompiler {

    public static CMaProgram compile(EstologProg prog) {
        CMaProgramWriter pw = new CMaProgramWriter();

        EstologAstVisitor<Void> visitor = new EstologAstVisitor<>() {
            private final List<String> variables = new ArrayList<>();

            @Override
            public Void visit(EstologLiteraal literaal) {
                pw.visit(LOADC, CMaUtils.bool2int(literaal.getValue()));
                return null;
            }

            @Override
            public Void visit(EstologMuutuja muutuja) {
                int variableIndex = variables.indexOf(muutuja.getNimi());
                if (variableIndex < 0) throw new NoSuchElementException("Undefined variable " + muutuja.getNimi());
                pw.visit(LOADA, variableIndex);
                return null;
            }

            @Override
            public Void visit(EstologJa ja) {
                visit(ja.getLeftChild());
                visit(ja.getRightChild());
                pw.visit(AND);
                return null;
            }

            @Override
            public Void visit(EstologVoi voi) {
                visit(voi.getLeftChild());
                visit(voi.getRightChild());
                pw.visit(OR);
                return null;
            }

            @Override
            public Void visit(EstologVordus vordus) {
                visit(vordus.getLeftChild());
                visit(vordus.getRightChild());
                pw.visit(EQ);
                return null;
            }

            @Override
            public Void visit(EstologKui kui) {
                CMaLabel _else = new CMaLabel();
                CMaLabel _endif = new CMaLabel();
                visit(kui.getKuiAvaldis());
                pw.visit(JUMPZ, _else);
                visit(kui.getSiisAvaldis());
                pw.visit(JUMP, _endif);
                pw.visit(_else);
                if (kui.getMuiduAvaldis() != null)
                    visit(kui.getMuiduAvaldis());
                else
                    pw.visit(LOADC, CMaUtils.bool2int(true));
                pw.visit(_endif);
                return null;
            }

            @Override
            public Void visit(EstologDef def) {
                visit(def.getAvaldis());

                int variableIndex = variables.indexOf(def.getNimi());
                if (variableIndex >= 0) {
                    pw.visit(STOREA, variableIndex);
                    pw.visit(POP);
                }
                else {
                    variables.add(def.getNimi());
                }
                return null;
            }

            @Override
            public Void visit(EstologProg prog) {
                for (EstologDef def : prog.getDefs()) {
                    visit(def);
                }
                visit(prog.getAvaldis());
                return null;
            }
        };

        visitor.visit(prog);

        return pw.toProgram();
    }

    public static void main(String[] args) throws IOException {
        EstologProg prog = prog(
                kui(vordus(var("x"), var("y")), var("a"), var("b")),

                def("x", lit(false)),
                def("y", lit(true)),
                def("a", ja(var("x"), var("y"))),
                def("b", voi(var("x"), var("y")))
        );

        // väärtustame otse
        System.out.printf("eval: %b%n", EstologEvaluator.eval(prog));

        // kompileeri avaldist arvutav CMa programm
        CMaProgram program = compile(prog);

        // kirjuta programm faili, mida saab Vam-iga vaadata
        CMaStack initialStack = new CMaStack();
        program.toFile(Paths.get("cmas", "estolog.cma"), initialStack);

        // interpreteeri CMa programm
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);
        System.out.printf("compiled: %d%n", finalStack.peek());
        System.out.printf("finalStack: %s%n", finalStack);
    }
}
