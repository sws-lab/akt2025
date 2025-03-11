package toylangs.sholog;

import toylangs.sholog.ast.*;

import java.util.Map;

public class ShologEvaluator {

    public static class ShologException extends RuntimeException {
        private final int code;

        public ShologException(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String getMessage() {
            return "code " + code;
        }
    }

    /**
     * Defineerimata muutuja veakood.
     */
    private static final int UNDEFINED_CODE = 127;


    public static boolean eval(ShologNode node, Map<String, Boolean> env) {
        throw new UnsupportedOperationException();
    }
}
