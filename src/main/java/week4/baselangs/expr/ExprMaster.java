package week4.baselangs.expr;

import week4.baselangs.expr.ast.*;

import java.util.List;

public class ExprMaster {

    // Kõik avaldise arvud panna listi
    public static List<Integer> valueList(ExprNode expr) {
        return null;
    }

    //Jagamised asendada nende väärtustega...
    public static ExprNode replaceDivs(ExprNode expr) {
        return null;
    }
    
        
    // Nüüd kokku koguda arvud niimoodi, et summa jääks sama nagu avaldises..
    public static List<Integer> signedValueList(ExprNode expr) {
        return null;
    }
    
    
    // Miinuste elimineerimine:
    public static ExprNode elimNeg(ExprNode expr) {
        return null;
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

