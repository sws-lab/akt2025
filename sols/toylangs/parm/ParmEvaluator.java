package toylangs.parm;

import toylangs.parm.ast.*;

import java.util.HashMap;
import java.util.Map;

public class ParmEvaluator {

    public static int eval(ParmNode expr) {
        return new ParmAstVisitor<Integer>() {
            Map<String, Integer> env = new HashMap<>();

            @Override
            protected Integer visit(ParmLit lit) {
                return lit.getValue();
            }

            @Override
            protected Integer visit(ParmVar var) {
                return env.get(var.getName());
            }

            @Override
            protected Integer visit(ParmPlus plus) {
                return visit(plus.getLeftExpr()) + visit(plus.getRightExpr());
            }

            @Override
            protected Integer visit(ParmUpdate up) {
                Integer val = visit(up.getValue());
                env.put(up.getVariableName(), val);
                return val;
            }

            @Override
            protected Integer visit(ParmCompose comp) {
                if (comp.isParallel()) {
                    Map<String, Integer> updates = computeUpdatesAndRestoreEnv(comp.getFst());
                    Integer result = visit(comp.getSnd());
                    env.putAll(updates);
                    return result;
                } else {
                    visit(comp.getFst());
                    return visit(comp.getSnd());
                }
            }

            /**
             * Abimeetod paralleelse komponeerimise jaoks.
             * Väärtustab avaldist ja taastab keskkonda.
             * Tagastab ainult muutujad, mille väärtus on muutunud.
             */
            private Map<String, Integer> computeUpdatesAndRestoreEnv(ParmNode expr) {
                Map<String, Integer> backup = new HashMap<>(env);
                visit(expr);

                // updates = env - backup
                env.entrySet().removeAll(backup.entrySet());
                Map<String, Integer> updates = env;

                env = backup;
                return updates;
            }
        }.visit(expr);
    }



}
