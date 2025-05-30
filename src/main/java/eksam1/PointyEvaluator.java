package eksam1;

import eksam1.ast.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointyEvaluator {

    public static Map<String, Integer> eval(PointyNode node, Map<String, Integer> initialEnv, List<String> variables) {
        Map<String, Integer> env = new HashMap<>(initialEnv); // teeme koopia, sest initialEnv pole muudetav
        return env;
    }
}
