package toylangs.pullet;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import toylangs.pullet.ast.PulletBinding;
import toylangs.pullet.ast.PulletNode;
import toylangs.pullet.ast.PulletSum;
import utils.ExceptionErrorListener;

import static toylangs.pullet.ast.PulletNode.*;
import static toylangs.pullet.PulletParser.*;

public class PulletAst {

    /**
     * Vabrikumeetod avaldise abstraktse süntaksipuu loomiseks sõnest.
     * Jooksutab esmalt ANTLR-i ja kutsub allolev abimeetod, mida tuleks implementeerida.
     * NB! Ära unusta ANTLR-i koodi uuesti genereerida peale grammatika muutmist.
     */
    public static PulletNode makePulletAst(String input) {
        PulletLexer lexer = new PulletLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        PulletParser parser = new PulletParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree konkreetnePuu = parser.init();
        //System.out.println(konkreetnePuu.toStringTree(parser));
        return parseTreeToAst(konkreetnePuu);
    }


    private static PulletNode parseTreeToAst(ParseTree tree) {
        PulletBaseVisitor<PulletNode> visitor = new PulletBaseVisitor<>() {
            @Override
            public PulletNode visitInit(InitContext ctx) {
                return visit(ctx.expr());
            }

            @Override
            public PulletNode visitBinding(BindingContext ctx) {
                PulletNode binding = visit(ctx.body);
                for (int i = ctx.Var().size()-1; i >= 0; i--) {
                    String name = ctx.Var(i).getText();
                    PulletNode value = visit(ctx.expr(i));
                    binding = new PulletBinding(name, value, binding);
                }
                return binding;
            }

            @Override
            public PulletNode visitDiff(DiffContext ctx) {
                return diff(visit(ctx.expr(0)), visit(ctx.expr(1)));
            }

            @Override
            public PulletNode visitVar(VarContext ctx) {
                return var(ctx.getText());
            }

            @Override
            public PulletNode visitNum(NumContext ctx) {
                return num(Integer.parseInt(ctx.getText()));
            }

            @Override
            public PulletNode visitSum(SumContext ctx) {
                PulletNode sum = visit(ctx.body);
                for (int i = ctx.Var().size() - 1; i >= 0; i--) {
                    String name = ctx.Var(i).getText();
                    PulletNode lo = visit(ctx.lo.get(i));
                    PulletNode hi = visit(ctx.hi.get(i));
                    sum = new PulletSum(name, lo, hi, sum);
                }
                return sum;
            }

            @Override
            public PulletNode visitParen(ParenContext ctx) {
                return visit(ctx.expr());
            }
        };
        return visitor.visit(tree);
    }
}
