package eksam1;

import eksam1.ast.PointyNode;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import utils.ExceptionErrorListener;

import static eksam1.ast.PointyNode.*;

public class PointyAst {

    public static PointyNode makePointyAst(String input) {
        PointyLexer lexer = new PointyLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        PointyParser parser = new PointyParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        return parseTreeToAst(tree);
    }

    private static PointyNode parseTreeToAst(ParseTree tree) {
        throw new UnsupportedOperationException();
    }
}
