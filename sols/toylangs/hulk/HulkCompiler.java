package toylangs.hulk;

import cma.CMaLabel;
import cma.CMaProgram;
import cma.CMaProgramWriter;
import cma.CMaUtils;
import toylangs.hulk.ast.*;
import toylangs.hulk.ast.expressions.HulkBinOp;
import toylangs.hulk.ast.expressions.HulkLit;
import toylangs.hulk.ast.expressions.HulkVar;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static cma.instruction.CMaLabelInstruction.Code.JUMPZ;

public class HulkCompiler {

    private static final List<Character> SET_VARIABLES = Arrays.asList('X', 'A', 'B', 'C', 'D', 'G', 'H', 'V'); // kasuta SET_VARIABLES.indexOf

    // kasuta hulga literaali teisendamiseks arvuks (bitset)
    private static int set2int(Set<Character> set) {
        List<Character> ELEM_VARIABLES = Arrays.asList('x', 'y', 'z', 'a', 'b', 'c', 'u', 'v');
        int result = 0;
        if (set != null) {
            for (int i = 0; i < ELEM_VARIABLES.size(); i++) {
                result |= CMaUtils.bool2int(set.contains(ELEM_VARIABLES.get(i))) << i;
            }
        }
        return result;
    }

    public static CMaProgram compile(HulkNode node) {
        CMaProgramWriter pw = new CMaProgramWriter();

        HulkAstVisitor<Void> visitor = new HulkAstVisitor<>() {
            @Override
            public Void visit(HulkLit lit) {
                pw.visit(LOADC, set2int(lit.getElements()));
                return null;
            }

            @Override
            public Void visit(HulkVar variable) {
                pw.visit(LOADA, SET_VARIABLES.indexOf(variable.getName()));
                return null;
            }

            @Override
            public Void visit(HulkBinOp binop) {
                visit(binop.getLeft());
                visit(binop.getRight());
                switch (binop.getOp()) {
                    case '+':
                        pw.visit(OR);
                        break;
                    case '-':
                        // A - B = A & ~B = A & (B ^ 11..1) = A & (B ^ -1)
                        pw.visit(LOADC, -1);
                        pw.visit(XOR);
                        pw.visit(AND);
                        break;
                    case '&':
                        pw.visit(AND);
                        break;
                }
                return null;
            }

            @Override
            public Void visit(HulkStmt stmt) {
                if (stmt.getCond() == null)
                    pw.visit(LOADC, 1);
                else
                    visit(stmt.getCond());

                CMaLabel _endif = new CMaLabel();
                pw.visit(JUMPZ, _endif);

                visit(stmt.getExpr());
                pw.visit(STOREA, SET_VARIABLES.indexOf(stmt.getName()));
                pw.visit(POP);

                pw.visit(_endif);
                return null;
            }

            @Override
            public Void visit(HulkProg programm) {
                for (HulkStmt hulkStmt : programm.getStatements()) {
                    visit(hulkStmt);
                }
                return null;
            }

            @Override
            public Void visit(HulkCond cond) {
                visit(cond.getSuperset());
                pw.visit(DUP);

                visit(cond.getSubset());

                // võtame ühendi mõlemast hulgast
                pw.visit(OR);

                // kontrollime, kas ühend on sama mis ülemhulk
                pw.visit(EQ);
                return null;
            }
        };

        visitor.visit(node);

        return pw.toProgram();
    }
}
