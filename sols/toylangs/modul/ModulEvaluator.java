package toylangs.modul;

import toylangs.modul.ast.*;

import java.util.Map;

public class ModulEvaluator {

    public static int eval(ModulProg prog, Map<String, Integer> env) {
        int m = prog.getModulus();

        return new Visitor(m, env).visit(prog);
    }

    // Eraldi klass, et ModulMaster saaks taaskasutada.
    static class Visitor extends ModulAstVisitor<Integer> {
        protected final int m;
        protected final Map<String, Integer> env;

        public Visitor(int m, Map<String, Integer> env) {
            this.m = m;
            this.env = env;
        }

        /**
         * Normaliseerib väärtuse poollõiku [0, m).
         */
        protected int normalize(int value) {
            return ((value % m) + m) % m; // negatiivsete arvude jaoks keerulisem
        }

        @Override
        protected Integer visit(ModulNum num) {
            return normalize(num.getValue());
        }

        @Override
        protected Integer visit(ModulVar var) {
            Integer value = env.get(var.getName());
            if (value == null)
                throw new ModulException();
            else
                return normalize(value);
        }

        @Override
        protected Integer visit(ModulNeg neg) {
            return normalize(-visit(neg.getExpr()));
        }

        @Override
        protected Integer visit(ModulAdd add) {
            return normalize(visit(add.getLeft()) + visit(add.getRight()));
        }

        @Override
        protected Integer visit(ModulMul mul) {
            return normalize(visit(mul.getLeft()) * visit(mul.getRight()));
        }

        @Override
        protected Integer visit(ModulPow pow) {
            Integer base = visit(pow.getBase());
            int result = 1;
            for (int i = 0; i < pow.getPower(); i++) {
                result = normalize(result * base);
            }
            return result;
        }

        @Override
        protected Integer visit(ModulProg prog) {
            return visit(prog.getExpr());
        }
    }
}
