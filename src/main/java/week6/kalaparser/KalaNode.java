package week6.kalaparser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public abstract class KalaNode {

    public abstract int sum(Map<String, Integer> env);

    public static KalaNode mkNull() {
        return new KalaNull();
    }

    public static KalaNode mkIdent(String name) {
        return new KalaIdent(name);
    }

    public static KalaNode mkList(List<KalaNode> args) {
        return new KalaList(args);
    }

    public static KalaNode mkList(KalaNode... args) {
        return mkList(Arrays.asList(args));
    }


    // Siit edasi on siis lahendus ette antud, et selle peale mitte jälle liiga palju aega kulutada.
    // Nende peidetud "sisemiste" klasside asemel võid kodutöös teha täiesti eraldi avalikud klassid!

    public static class KalaIdent extends KalaNode {

        private final String name;

        public String getName() {
            return name;
        }

        public KalaIdent(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int sum(Map<String, Integer> env) {
            throw new UnsupportedOperationException();
        }
    }

    public static class KalaList extends KalaNode {

        private final List<KalaNode> args;

        public List<KalaNode> getArgs() {
            return args;
        }

        public KalaList(List<KalaNode> args) {
            this.args = args;
        }

        @Override
        public String toString() {
            StringJoiner joiner = new StringJoiner(", ", "(", ")");
            for (KalaNode arg : args) joiner.add(arg.toString());
            return joiner.toString();
        }

        @Override
        public int sum(Map<String, Integer> env) {
            return args.stream().mapToInt(n -> n.sum(env)).sum();
        }
    }

    public static class KalaNull extends KalaNode {

        @Override
        public String toString() {
            return "NULL";
        }

        @Override
        public int sum(Map<String, Integer> env) {
            return 0;
        }
    }

    public static abstract class KalaAstVisitor<T> {
        abstract protected T visit(KalaNull nullnode);
        abstract protected T visit(KalaList list);
        abstract protected T visit(KalaIdent ident);

        public T visit(KalaNode node) {
            if (node instanceof KalaNull nullnode) return visit(nullnode);
            if (node instanceof KalaList list) return visit(list);
            return visit(((KalaIdent) node));
        }

    }
}
