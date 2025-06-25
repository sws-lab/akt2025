package eksam3;

import eksam3.ast.SimpNode;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import utils.ExceptionErrorListener;

import java.io.IOException;
import java.nio.file.Paths;

import static eksam3.ast.SimpNode.*;

public class SimpAst {

    public static SimpNode makeSimpAst(String input) {
        SimpLexer lexer = new SimpLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        SimpParser parser = new SimpParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        return parseTreeToAst(tree);
    }

    private static SimpNode parseTreeToAst(ParseTree tree) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) throws IOException {
        SimpNode ast = makeSimpAst("5; m + 1;");
        ast.renderPngFile(Paths.get("graphs", "simp.png"));
        System.out.println(ast);
    }
}
