package week6;

import week5.AktkToken;

import java.util.Map;

import static week5.AktkToken.Type.*;

public abstract class AktkNode {

    public static AktkNode num(int value) {
        return new IntLiteral(value);
    }

    public static AktkNode var(String name) {
        return new Variable(name);
    }

    private static AktkNode binop(AktkToken.Type type, AktkNode left, AktkNode right) {
        return new BinOp(type, left, right);
    }

    public static AktkNode plus(AktkNode left, AktkNode right) {
        return binop(PLUS, left, right);
    }

    public static AktkNode minus(AktkNode left, AktkNode right) {
        return binop(MINUS, left, right);
    }

    public static AktkNode mul(AktkNode left, AktkNode right) {
        return binop(TIMES, left, right);
    }

    public static AktkNode div(AktkNode left, AktkNode right) {
        return binop(DIV, left, right);
    }

    public abstract int eval(Map<String, Integer> env);

    public String toPrettyString() {
        return toPrettyString(0);
    }

    protected abstract String toPrettyString(int contextPriority);

    public static class IntLiteral extends AktkNode {
        final private int value;

        public int getValue() {
            return value;
        }

        public IntLiteral(int value) {
            this.value = value;
        }

        @Override
        public int eval(Map<String, Integer> env) {
            return value;
        }


        @Override
        public String toString() {
            return "num(" + value + ")";
        }

        @Override
        protected String toPrettyString(int contextPriority) {
            return Integer.toString(value);
        }
    }

    public static class Variable extends AktkNode {

        private final String name;

        public Variable(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public int eval(Map<String, Integer> env) {
            return env.get(getName());
        }


        @Override
        public String toString() {
            return "var(\"" + name + "\")";
        }


        @Override
        protected String toPrettyString(int contextPriority) {
            return name;
        }
    }

    public static class BinOp extends AktkNode {

        private final AktkToken.Type op;

        private final AktkNode left;
        private final AktkNode right;

        public BinOp(AktkToken.Type token, AktkNode left, AktkNode right) {
            if (!isOperator(token)) throw new IllegalArgumentException();
            this.op = token;
            this.left = left;
            this.right = right;
        }

        private boolean isOperator(AktkToken.Type token) {
            return switch (token) {
                case PLUS, MINUS, TIMES, DIV -> true;
                default -> false;
            };
        }

        @Override
        public int eval(Map<String, Integer> env) {
            int l = left.eval(env);
            int r = right.eval(env);
            return switch (op) {
                case PLUS -> l + r;
                case MINUS -> l - r;
                case TIMES -> l * r;
                case DIV -> l / r;
                default -> throw new AssertionError("Impossible!");
            };
        }


        @Override
        public String toString() {
            return op.toString().toLowerCase() + "(" + left + ", " + right + ")";
        }

        @Override
        protected String toPrettyString(int contextPriority) {
            int prio = priorityOf(op);
            int assoc = prio + (isAssoc(op) ? 0 : 1);
            String content = left.toPrettyString(prio) + symbolOf(op) + right.toPrettyString(assoc);
            if (contextPriority > prio) return "(" + content + ")";
            else return content;
        }

        private boolean isAssoc(AktkToken.Type op) {
            return switch (op) {
                case MINUS, DIV -> false;
                default -> true;
            };
        }

        private String symbolOf(AktkToken.Type op) {
            return switch (op) {
                case PLUS -> "+";
                case MINUS -> "-";
                case TIMES -> "*";
                case DIV -> "/";
                default -> null;
            };
        }

        private int priorityOf(AktkToken.Type op) {
            return switch (op) {
                case PLUS, MINUS -> 1;
                case TIMES, DIV -> 2;
                default -> 0;
            };
        }
    }
}
