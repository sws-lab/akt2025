package toylangs.imp;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import toylangs.imp.ast.ImpNode;
import utils.ExceptionErrorListener;

import java.io.IOException;
import java.nio.file.Paths;

import static toylangs.imp.ast.ImpNode.*;

public class ImpAst {

    public static ImpNode makeImpAst(String input) {
        ImpLexer lexer = new ImpLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        ImpParser parser = new ImpParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        //System.out.println(tree.toStringTree(parser));
        return parseTreeToAst(tree);
    }

    private static ImpNode parseTreeToAst(ParseTree tree) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) throws IOException {
        ImpNode ast = makeImpAst("x = 5, x + 1");
        ast.renderPngFile(Paths.get("graphs", "imp.png"));
        System.out.println(ast);
    }
}
