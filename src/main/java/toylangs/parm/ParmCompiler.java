package toylangs.parm;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.parm.ast.*;

import java.util.Arrays;
import java.util.List;

import static toylangs.parm.ast.ParmNode.lit;
import static toylangs.parm.ast.ParmNode.plus;

public class ParmCompiler {

    public static final List<String> VARS = Arrays.asList("X", "Y", "Z", "A", "B", "C", "D"); // kasuta VARS.indexOf

    public static CMaProgram compile(ParmNode node) {
        CMaProgramWriter pw = new CMaProgramWriter();
        return pw.toProgram();
    }

    public static void main(String[] args) {
        CMaProgram cMaProgram = compile(plus(lit(10), lit(12)));
        System.out.println(cMaProgram.toString());
    }
}
