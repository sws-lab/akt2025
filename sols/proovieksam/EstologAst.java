package proovieksam;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import proovieksam.ast.EstologDef;
import proovieksam.ast.EstologNode;
import utils.ExceptionErrorListener;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static proovieksam.EstologParser.*;
import static proovieksam.ast.EstologNode.*;

public class EstologAst {

    public static void main(String[] args) throws IOException {
        EstologNode ast = makeEstologAst("""
                x := 0;
                y := 1;
                a := (x JA y);
                b := (x VOI y);

                (KUI (x = y) SIIS a MUIDU b)""");
        System.out.println(ast);
        ast.renderPngFile(Paths.get("graphs", "estolog.png"));
    }

    public static EstologNode makeEstologAst(String input) {
        EstologLexer lexer = new EstologLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        EstologParser parser = new EstologParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        //System.out.println(tree.toStringTree(parser));
        return parseTreeToAst(tree);
    }

    // Implementeeri see meetod.
    private static EstologNode parseTreeToAst(ParseTree tree) {
        EstologBaseVisitor<EstologNode> visitor = new EstologBaseVisitor<>() {
            @Override
            public EstologNode visitInit(InitContext ctx) {
                return visit(ctx.prog());
            }

            @Override
            public EstologNode visitProg(ProgContext ctx) {
                List<EstologDef> defs = new ArrayList<>();
                for (DefContext defContext : ctx.def()) {
                    defs.add(visitDef(defContext)); // otse õige visit kutse
                }
                EstologNode avaldis = visit(ctx.avaldis());
                return prog(avaldis, defs);
            }

            @Override
            public EstologDef visitDef(DefContext ctx) {
                // täpsustatud tagastustüüp
                return def(ctx.Muutuja().getText(), visit(ctx.avaldis()));
            }

            @Override
            public EstologNode visitLiteraal(LiteraalContext ctx) {
                return lit(ctx.getText().equals("1"));
            }

            @Override
            public EstologNode visitMuutuja(MuutujaContext ctx) {
                return var(ctx.getText());
            }

            @Override
            public EstologNode visitSulud(SuludContext ctx) {
                return visit(ctx.avaldis());
            }

            @Override
            public EstologNode visitBinOp(BinOpContext ctx) {
                EstologNode left = visit(ctx.getChild(0));
                EstologNode right = visit(ctx.getChild(2));
                return switch (ctx.getChild(1).getText()) {
                    case "=" -> vordus(left, right);
                    case "VOI" -> voi(left, right);
                    case "JA", "NING" -> ja(left, right);
                    default -> throw new UnsupportedOperationException();
                };
            }

            @Override
            public EstologNode visitKuiSiis(KuiSiisContext ctx) {
                EstologNode kui = visit(ctx.avaldis(0));
                EstologNode siis = visit(ctx.avaldis(1));
                EstologNode muidu = null;
                if (ctx.avaldis(2) != null) muidu = visit(ctx.avaldis(2));
                return kui(kui, siis, muidu);
            }

            // Lihtne käsitletakse BaseVisitor-is vaikimisi
        };

        return visitor.visit(tree);
    }
}
