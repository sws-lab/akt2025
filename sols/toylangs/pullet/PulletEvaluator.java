package toylangs.pullet;

import toylangs.pullet.ast.*;
import week8.Environment;

public class PulletEvaluator {

    // Vana hea eval meetod... tuleb avaldist väärtustada!
    public static int eval(PulletNode expr) {
        PulletAstVisitor<Integer> visitor = new PulletAstVisitor<>() {
            private final Environment<Integer> env = new Environment<>();

            @Override
            protected Integer visit(PulletNum num) {
                return num.getNum();
            }

            @Override
            protected Integer visit(PulletVar var) {
                return env.get(var.getName());
            }

            @Override
            protected Integer visit(PulletBinding binding) {
                Integer value = visit(binding.getValue());
                env.enterBlock();
                env.declareAssign(binding.getName(), value);
                Integer result = visit(binding.getBody());
                env.exitBlock();
                return result;
            }

            @Override
            protected Integer visit(PulletSum sum) {
                int from = visit(sum.getLo());
                int to = visit(sum.getHi());
                int result = 0;
                for (int i = from; i <= to; i++) {
                    env.enterBlock();
                    env.declareAssign(sum.getName(), i);
                    result += visit(sum.getBody());
                    env.exitBlock();
                }
                return result;
            }

            @Override
            protected Integer visit(PulletDiff diff) {
                return visit(diff.getLeft()) - visit(diff.getRight());
            }
        };

        return visitor.visit(expr);
    }
}
