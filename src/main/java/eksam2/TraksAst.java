package eksam2;

import eksam2.ast.TraksStmt;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import utils.ExceptionErrorListener;

import static eksam2.ast.TraksNode.*;

public class TraksAst {

    public static TraksStmt makeTraksAst(String input) {
        TraksLexer lexer = new TraksLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        TraksParser parser = new TraksParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        return parseTreeToAst(tree);
    }

    private static TraksStmt parseTreeToAst(ParseTree tree) {
        throw new UnsupportedOperationException();
    }
}
