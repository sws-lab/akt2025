package toylangs.safdi;

import toylangs.safdi.ast.SafdiNode;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import utils.ExceptionErrorListener;

import static toylangs.safdi.ast.SafdiNode.*;

public class SafdiAst {

    public static SafdiNode makeSafdiAst(String input) {
        SafdiLexer lexer = new SafdiLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        SafdiParser parser = new SafdiParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        return parseTreeToAst(tree);
    }

    private static SafdiNode parseTreeToAst(ParseTree tree) {
        throw new UnsupportedOperationException();
    }
}
