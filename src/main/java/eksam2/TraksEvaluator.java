package eksam2;

import eksam2.ast.*;

import java.util.HashMap;
import java.util.Map;

public class TraksEvaluator {

    public static Map<String, Integer> eval(TraksStmt stmt, Map<String, Integer> initialEnv) {
        Map<String, Integer> env = new HashMap<>(initialEnv); // teeme koopia, sest initialEnv pole muudetav
        return env;
    }

    /**
     * Erind, mille viskamise ja püüdmise abil saab katkestamislauseid ja transaktsioone käsitleda.
     */
    private static class CheckFailedException extends RuntimeException {

    }
}
