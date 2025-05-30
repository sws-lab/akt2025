package eksam2;

import cma.CMaProgram;
import cma.CMaProgramWriter;
import eksam2.ast.*;

import java.util.List;

import static cma.instruction.CMaBasicInstruction.Code.*;
import static cma.instruction.CMaIntInstruction.Code.*;
import static cma.instruction.CMaLabelInstruction.Code.*;

public class TraksCompiler {

    public static CMaProgram compile(TraksStmt stmt, List<String> variables) {
        TraksProgramWriter pw = new TraksProgramWriter(variables.size());

        return pw.toProgram();
    }

    /**
     * Klass, mille abil saab terviklike keskkondadega opereerida.
     */
    private static class TraksProgramWriter extends CMaProgramWriter {
        /**
         * Keskkonna suurus (muutujate arv).
         */
        private final int size;

        public TraksProgramWriter(int size) {
            this.size = size;
        }

        /**
         * Kopeerib praeguse keskkonna stack'i peale.
         */
        public void backupEnv() {
            for (int i = 0; i < size; i++)
                visit(LOADA, i);
        }

        /**
         * Asendab praeguse keskkonna stack'i-pealsega ja eemaldab koopia stack'i pealt.
         */
        public void restoreEnv() {
            for (int i = size - 1; i >= 0; i--) {
                visit(STOREA, i);
                visit(POP);
            }
        }

        /**
         * Eemaldab keskkonna koopia stack'i pealt.
         */
        public void popEnv() {
            for (int i = size - 1; i >= 0; i--)
                visit(POP);
        }
    }
}
