package proovieksam;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import proovieksam.ast.EstologNode;
import utils.ExceptionErrorListener;

import java.io.IOException;
import java.nio.file.Paths;

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
        throw new UnsupportedOperationException();
    }
}
