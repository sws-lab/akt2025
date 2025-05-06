package toylangs.sholog;

import cma.CMaLabel;
import cma.CMaProgram;
import cma.CMaProgramWriter;
import cma.CMaUtils;
import toylangs.sholog.ast.*;

import java.util.List;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static cma.instruction.CMaLabelInstruction.Code.*;

public class ShologCompiler {

    /**
     * Defineerimata muutuja veakood.
     */
    private static final int UNDEFINED_CODE = 127;


    public static CMaProgram compile(ShologNode node, List<String> variables) {
        CMaProgramWriter pw = new CMaProgramWriter();

        new ShologAstVisitor<Void>() {
            @Override
            protected Void visit(ShologLit lit) {
                pw.visit(LOADC, CMaUtils.bool2int(lit.getValue()));
                return null;
            }

            @Override
            protected Void visit(ShologVar var) {
                int variableIndex = variables.indexOf(var.getName());
                if (variableIndex < 0)
                    visitThrow(UNDEFINED_CODE);
                else
                    pw.visit(LOADA, variableIndex);
                return null;
            }

            /**
             * Viska CMa-s erind kokkulepitud viisil.
             */
            private void visitThrow(int code) {
                pw.visit(LOADC, -code);
                pw.visit(HALT);
            }

            @Override
            protected Void visit(ShologError error) {
                visitThrow(error.getCode());
                return null;
            }

            @Override
            protected Void visit(ShologEager eager) {
                visit(eager.getLeft());
                visit(eager.getRight());
                switch (eager.getOp()) {
                    case And -> pw.visit(AND);
                    case Or -> pw.visit(OR);
                    case Xor -> pw.visit(XOR);
                    default -> throw new UnsupportedOperationException("unknown eager op");
                }
                return null;
            }

            @Override
            protected Void visit(ShologLazy lazy) {
                visit(lazy.getLeft());
                CMaLabel shortCircuit = new CMaLabel();

                pw.visit(DUP); // duubelda vasaku argumendi väärtus
                switch (lazy.getOp()) {
                    case And:
                        // kasuta pealmist vasaku argumendi väärtust kontrolliks
                        pw.visit(JUMPZ, shortCircuit);
                        // kasuta alumist vasaku argumendi väärtust tehteks
                        visit(lazy.getRight());
                        pw.visit(AND);
                        break;
                    case Or:
                        // kasuta pealmist vasaku argumendi väärtust kontrolliks
                        pw.visit(NOT);
                        pw.visit(JUMPZ, shortCircuit);
                        // kasuta alumist vasaku argumendi väärtust tehteks
                        visit(lazy.getRight());
                        pw.visit(OR);
                        break;
                    default:
                        throw new UnsupportedOperationException("unknown lazy op");
                }

                pw.visit(shortCircuit);
                // short-circuit korral kasuta alumist vasaku argumendi väärtust otse
                return null;
            }
        }.visit(node);

        return pw.toProgram();
    }
}
