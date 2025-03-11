package toylangs.vhile;

import toylangs.vhile.ast.*;

import java.util.HashMap;
import java.util.Map;

public class VhileEvaluator {

    public static Map<String, Integer> eval(VhileStmt stmt, Map<String, Integer> initialEnv) {
        Map<String, Integer> env = new HashMap<>(initialEnv); // teeme koopia, sest initialEnv pole muudetav
        return env;
    }

    /**
     * Erind, mille viskamise ja püüdmise abil saab katkestamislauseid käsitleda.
     */
    private static class BreakException extends RuntimeException {
        private final int level;

        public BreakException(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }
}
