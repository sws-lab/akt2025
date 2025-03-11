package week4.baselangs.rnd;

import week4.baselangs.rnd.ast.*;

import java.util.*;

public class RndMaster {

    // Nüüd tagastada kõik võimalikud tulemusväärtused hulgana.
    public static Set<Integer> evalNondeterministic(RndNode expr) {
        return new RndVisitor<Set<Integer>>() {
            @Override
            protected Set<Integer> visit(RndNum num) {
                return Collections.singleton(num.getValue());
            }

            @Override
            protected Set<Integer> visit(RndNeg neg) {
                Set<Integer> result = new HashSet<>();
                for (Integer v : visit(neg.getExpr())) {
                    result.add(-v);
                }
                return result;
                //return visit(neg.getExpr()).stream().map(x -> -x).collect(Collectors.toSet());
            }

            @Override
            protected Set<Integer> visit(RndAdd add) {
                Set<Integer> result = new HashSet<>();
                for (Integer x : visit(add.getLeft())) {
                    for (Integer y : visit(add.getRight())) {
                        result.add(x + y);
                    }
                }
                return result;
                //return visit(add.getLeft()).stream().flatMap(x -> visit(add.getRight()).stream().map(y -> x + y)).collect(Collectors.toSet());
            }

            @Override
            protected Set<Integer> visit(RndFlip flip) {
                Set<Integer> result = new HashSet<>();
                result.addAll(visit(flip.getLeft()));
                result.addAll(visit(flip.getRight()));
                return result;
            }
        }.visit(expr);
    }

    // Tagastada avaldise võimalike tulemuste tõenäosusjaotus.
    public static Map<Integer, Double> evalDistribution(RndNode expr) {
        return new RndVisitor<Map<Integer, Double>>() {
            @Override
            protected Map<Integer, Double> visit(RndNum num) {
                HashMap<Integer, Double> map = new HashMap<>();
                map.put(num.getValue(), 1.0);
                return map;
            }

            @Override
            protected Map<Integer, Double> visit(RndNeg neg) {
                HashMap<Integer, Double> map = new HashMap<>();
                for (Map.Entry<Integer, Double> entry : visit(neg.getExpr()).entrySet()) {
                    map.put(-entry.getKey(), entry.getValue());
                }
                return map;
            }

            @Override
            protected Map<Integer, Double> visit(RndFlip flip) {
                HashMap<Integer, Double> map = new HashMap<>();
                for (Map.Entry<Integer, Double> entry : visit(flip.getLeft()).entrySet()) {
                    map.put(entry.getKey(), 0.5 * entry.getValue());
                }
                for (Map.Entry<Integer, Double> entry : visit(flip.getRight()).entrySet()) {
                    map.merge(entry.getKey(), 0.5 * entry.getValue(), Double::sum);
                }
                return map;
            }

            @Override
            protected Map<Integer, Double> visit(RndAdd add) {
                HashMap<Integer, Double> map = new HashMap<>();
                for (Map.Entry<Integer, Double> entry1 : visit(add.getLeft()).entrySet()) {
                    for (Map.Entry<Integer, Double> entry2 : visit(add.getRight()).entrySet()) {
                        // Jällegi merge, sest sama tulemuse korral tuleb liita tõenäosused.
                        map.merge(entry1.getKey() + entry2.getKey(), entry1.getValue() * entry2.getValue(), Double::sum);
                    }
                }
                return map;
            }
        }.visit(expr);
    }
}
