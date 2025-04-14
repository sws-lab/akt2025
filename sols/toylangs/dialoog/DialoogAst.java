package toylangs.dialoog;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import toylangs.dialoog.ast.DialoogDecl;
import toylangs.dialoog.ast.DialoogNode;
import utils.ExceptionErrorListener;

import java.util.ArrayList;
import java.util.List;

import static toylangs.dialoog.ast.DialoogNode.*;

public class DialoogAst {

    public static DialoogNode makeDialoogAst(String sisend) {
        DialoogLexer lexer = new DialoogLexer(CharStreams.fromString(sisend));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        DialoogParser parser = new DialoogParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        //System.out.println(tree.toStringTree(parser));
        return parseTreetoAst(tree);
    }

    // Implementeerida parsepuu -> AST teisendus!
    private static DialoogNode parseTreetoAst(ParseTree tree) {
        return new DialoogBaseVisitor<DialoogNode>() {

            @Override
            public DialoogNode visitInit(DialoogParser.InitContext ctx) {
                return visit(ctx.prog());
            }

            @Override
            public DialoogNode visitTvLit(DialoogParser.TvLitContext ctx) {
                return bl(ctx.getText().equals("jah"));
            }

            @Override
            public DialoogNode visitIntLit(DialoogParser.IntLitContext ctx) {
                return il(Integer.parseInt(ctx.getText()));
            }

            @Override
            public DialoogNode visitVar(DialoogParser.VarContext ctx) {
                return var(ctx.getText());
            }

            @Override
            public DialoogNode visitUnary(DialoogParser.UnaryContext ctx) {
                return unop(ctx.op.getText(), visit(ctx.expr()));
            }

            @Override
            public DialoogNode visitBinary(DialoogParser.BinaryContext ctx) {
                return binop(ctx.op.getText(), visit(ctx.expr(0)), visit(ctx.expr(1)));
            }

            @Override
            public DialoogNode visitIfte(DialoogParser.IfteContext ctx) {
                return ifte(visit(ctx.expr(0)), visit(ctx.expr(1)), visit(ctx.expr(2)));
            }

            @Override
            public DialoogNode visitParen(DialoogParser.ParenContext ctx) {
                return visit(ctx.expr());
            }

            @Override
            public DialoogNode visitProg(DialoogParser.ProgContext ctx) {
                List<DialoogDecl> decls = new ArrayList<>();
                for (DialoogParser.DeclContext declContext : ctx.decl()) {
                    boolean isInt = declContext.typ.getText().startsWith("int");
                    String varname = declContext.Var().getText();
                    decls.add(new DialoogDecl(varname, isInt));
                }
                return DialoogNode.prog(decls, visit(ctx.expr()));
            }
        }.visit(tree);
    }

    public static void main(String[] args) {
        String sisend =
                """
                        x on bool! y on int!
                        Arvuta:\s
                          2 *\s
                          Kas 5 + 5 on 10?\s
                            Jah: Kas 5 + 5 on 10?\s
                                   Jah: 35 * Oota Oota 2 + 2 Valmis - 3 Valmis
                                   Ei: 100\s
                                 Selge\s
                            Ei: 100\s
                          Selge\s
                        + 30 + Kas jah?
                                    Jah: 10
                                    Ei: 300
                               Selge""";

        System.out.println(makeDialoogAst(sisend));
    }

}


