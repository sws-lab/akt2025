package proovieksam;

import proovieksam.ast.*;
import proovieksam.ast.aatomid.*;
import proovieksam.ast.operaatorid.*;

import static proovieksam.ast.EstologNode.*;

public class EstologEvaluator {

    public static boolean eval(EstologProg prog) {
        throw new UnsupportedOperationException();
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
