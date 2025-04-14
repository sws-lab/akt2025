package toylangs.parm;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.parm.ast.ParmNode;

import static toylangs.parm.ast.ParmNode.lit;
import static toylangs.parm.ast.ParmNode.plus;

public class ParmMaster {

    public static CMaProgram compile(ParmNode node) {
        CMaProgramWriter pw = new CMaProgramWriter();
        return pw.toProgram();
    }

    public static void main(String[] args) {
        CMaProgram cMaProgram = compile(plus(lit(10), lit(12)));
        System.out.println(cMaProgram.toString());
    }
}
