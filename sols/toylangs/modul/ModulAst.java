package toylangs.modul;

import toylangs.modul.ast.*;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import utils.ExceptionErrorListener;

import static toylangs.modul.ModulParser.*;
import static toylangs.modul.ast.ModulNode.*;

public class ModulAst {

    public static ModulProg makeModulAst(String input) {
        ModulLexer lexer = new ModulLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        ModulParser parser = new ModulParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        return parseTreeToAst(parser.init());
    }

    private static ModulProg parseTreeToAst(InitContext tree) {
        // ParseTree asemel on siin täpsem tüüp: kõik töötab samamoodi! (võid isegi ignoreerida)
        // Saad aga mugvamalt teha tree.prog (sõltuvalt sinu reegli nimest) ja
        // need välised 1-2 juhtu ilma visitorita teha, et visitor saaks tagastada ModulExpr.

        ModulVisitor<ModulExpr> exprVisitor = new ModulBaseVisitor<>() {
            @Override
            public ModulExpr visitBinOp(BinOpContext ctx) {
                ModulExpr left = visit(ctx.left);
                ModulExpr right = visit(ctx.right);
                return switch (ctx.op.getText()) {
                    case "+" -> add(left, right);
                    case "-" -> sub(left, right);
                    case "*" -> mul(left, right);
                    default -> throw new UnsupportedOperationException("unknown op");
                };
            }

            @Override
            public ModulExpr visitNeg(NegContext ctx) {
                return neg(visit(ctx.expr()));
            }

            @Override
            public ModulExpr visitVar(VarContext ctx) {
                return var(ctx.Var().getText());
            }

            @Override
            public ModulExpr visitNum(NumContext ctx) {
                return num(Integer.parseInt(ctx.Num().getText()));
            }

            @Override
            public ModulExpr visitPow(PowContext ctx) {
                return pow(visit(ctx.expr()), Integer.parseInt(ctx.Num().getText()));
            }

            @Override
            public ModulExpr visitParen(ParenContext ctx) {
                return visit(ctx.expr());
            }
        };

        return prog(exprVisitor.visit(tree.prog().expr()),
                Integer.parseInt(tree.prog().Num().getText()));
    }
}
