package toylangs.sholog;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import toylangs.sholog.ast.*;

import java.util.List;

public class ShologCompiler {

    /**
     * Defineerimata muutuja veakood.
     */
    private static final int UNDEFINED_CODE = 127;


    public static CMaProgram compile(ShologNode node, List<String> variables) {
        CMaProgramWriter pw = new CMaProgramWriter();

        return pw.toProgram();
    }
}
