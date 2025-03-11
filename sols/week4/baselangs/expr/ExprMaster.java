package week4.baselangs.expr;

import week4.baselangs.expr.ast.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ExprMaster {

    // Kõik avaldise arvud panna listi
    public static List<Integer> valueList(ExprNode expr) {
        List<Integer> elems = new ArrayList<>();

        new ExprVisitor.BaseVisitor<Void>() {
            @Override
            public Void visit(ExprNum num) {
                elems.add(num.getValue());
                return null;
            }
        }.visit(expr);

        return elems;
    }

    //Jagamised asendada nende väärtustega...
    public static ExprNode replaceDivs(ExprNode expr) {
        return new ExprVisitor<ExprNode>() {
            @Override
            public ExprNode visit(ExprNum num) {
                // Võime lihtsalt sama tipu tagastada, kuna teda ei saa muuta.
                return num;
            }

            @Override
            protected ExprNode visit(ExprNeg neg) {
                // Siin peab koopia tegema, sest miinusmärgi all teeme teisendusi.
                return new ExprNeg(visit(neg.getExpr()));
            }

            @Override
            public ExprNode visit(ExprAdd add) {
                // Jällegi uus tipp teisendatud alampuudega.
                return new ExprAdd(visit(add.getLeft()), visit(add.getRight()));
            }

            @Override
            public ExprNode visit(ExprDiv div) {
                // Siin lõpuks toimub sisuline teisendus!
                return new ExprNum(div.eval());
            }
        }.visit(expr);
    }
    
        
    // Nüüd kokku koguda arvud niimoodi, et summa jääks sama nagu avaldises..
    public static List<Integer> signedValueList(ExprNode expr) {
        return new ExprVisitor<List<Integer>>() {

            @Override
            public List<Integer> visit(ExprAdd add) {
                List<Integer> list = new ArrayList<>();
                list.addAll(visit(add.getLeft()));
                list.addAll(visit(add.getRight()));
                return list;
            }

            @Override
            public List<Integer> visit(ExprNeg neg) {
                List<Integer> list = visit(neg.getExpr());
                // Kõikide elementide märke tuleb lihtsalt muuta:
                return list.stream().map(i -> -i).collect(Collectors.toList());
            }

            @Override
            public List<Integer> visit(ExprNum num) {
                return Collections.singletonList(num.getValue());
            }

            @Override
            public List<Integer> visit(ExprDiv div) {
                throw new UnsupportedOperationException();
            }

        }.visit(expr);
    }
    
    
    // Miinuste elimineerimine:
    public static ExprNode elimNeg(ExprNode expr) {
        return elimNeg(expr, 1);
    }

    private static ExprNode elimNeg(ExprNode expr, int sign) {
        return new ExprVisitor<ExprNode>() {
            @Override
            public ExprNode visit(ExprNeg neg) {
                return elimNeg(neg.getExpr(), -sign);
            }

            @Override
            public ExprNode visit(ExprNum num) {
                return new ExprNum(sign * num.getValue());
            }

            @Override
            public ExprNode visit(ExprDiv div) {
                return new ExprDiv(visit(div.getNumerator()), elimNeg(div.getDenominator(), 1));
            }

            @Override
            public ExprNode visit(ExprAdd add) {
                return new ExprAdd(visit(add.getLeft()), visit(add.getRight()));
            }

        }.visit(expr);
    }

    // Siin on näide aritmeetiliste avaldiste ilutrükki kohta.
    public static String pretty(ExprNode expr) {
        ExprVisitor<String> prettyExprVisitor = new ExprVisitor<>() {
            @Override
            public String visit(ExprNum num) {
                return Integer.toString(num.getValue());
            }

            @Override
            public String visit(ExprNeg neg) {
                return "-" + visit(neg.getExpr(), priorityOf(neg));
            }

            @Override
            public String visit(ExprAdd add) {
                return visit(add.getLeft(), priorityOf(add)) + "+" + visit(add.getRight(), priorityOf(add));
            }

            @Override
            public String visit(ExprDiv div) {
                return visit(div.getNumerator(), priorityOf(div)) + "/" +
                        visit(div.getDenominator(), priorityOf(div) + 1); // <-- vasakassotsiatiivsus;
            }

            // Põhiline abimeetod, mis otsustab konteksti põhjal ära, kas sulud on vaja või mitte:
            private String visit(ExprNode expr, int contextPriority) {
                String output = super.visit(expr);
                if (priorityOf(expr) < contextPriority) output = '(' + output + ')';
                return output;
            }

        };
        return prettyExprVisitor.visit(expr);
    }


    private static int priorityOf(ExprNode expr) {
        if (expr instanceof ExprAdd) return 1;
        if (expr instanceof ExprDiv) return 2;
        if (expr instanceof ExprNeg) return 3;
        return 4;
    }
}

