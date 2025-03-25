package proovieksam.lahendused;

import proovieksam.ast.EstologDef;
import proovieksam.ast.EstologKui;
import proovieksam.ast.EstologNode;
import proovieksam.ast.EstologProg;
import proovieksam.ast.aatomid.EstologLiteraal;
import proovieksam.ast.aatomid.EstologMuutuja;
import proovieksam.ast.operaatorid.EstologJa;
import proovieksam.ast.operaatorid.EstologVoi;
import proovieksam.ast.operaatorid.EstologVordus;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static proovieksam.ast.EstologNode.*;

public class EstologEvaluatorNoVisitor {
    private final Map<String, Boolean> env = new HashMap<>();

    public static boolean eval(EstologProg prog) {
        EstologEvaluatorNoVisitor evaluator = new EstologEvaluatorNoVisitor();
        return evaluator.visit(prog);
    }

    private Boolean visit(EstologNode node) {
        switch (node) {
            case EstologLiteraal literaal -> {
                return literaal.getValue();
            }
            case EstologMuutuja muutuja -> {
                Boolean value = env.get(muutuja.getNimi());
                if (value == null)
                    throw new NoSuchElementException("Undefined variable " + muutuja.getNimi());
                else
                    return value;
            }
            case EstologJa ja -> {
                return visit(ja.getLeftChild()) && visit(ja.getRightChild());
            }
            case EstologVoi voi -> {
                return visit(voi.getLeftChild()) || visit(voi.getRightChild());
            }
            case EstologVordus vordus -> {
                return visit(vordus.getLeftChild()) == visit(vordus.getRightChild());
            }
            case EstologKui kui -> {
                if (visit(kui.getKuiAvaldis())) {
                    return visit(kui.getSiisAvaldis());
                }
                else if (kui.getMuiduAvaldis() != null) {
                    return visit(kui.getMuiduAvaldis());
                }
                else {
                    return true; // kui MUIDU avaldis on puudu, tagastame true
                }
            }
            case EstologDef def -> {
                env.put(def.getNimi(), visit(def.getAvaldis()));
                return null;
            }
            case EstologProg prog -> {
                for (EstologDef def : prog.getDefs())
                    visit(def);
                return visit(prog.getAvaldis());
            }
            case null, default -> throw new UnsupportedOperationException("unknown node");
        }
    }

    public static void main(String[] args) {
        EstologProg prog = prog(
                kui(vordus(var("x"), var("y")), var("a"), var("b")),

                def("x", lit(false)),
                def("y", lit(true)),
                def("a", ja(var("x"), var("y"))),
                def("b", voi(var("x"), var("y")))
        );

        System.out.println(prog);
        System.out.println(eval(prog));
    }
}
