package toylangs.parm;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import toylangs.parm.ast.ParmNode;
import utils.ExceptionErrorListener;

import java.io.IOException;
import java.nio.file.Paths;

import static toylangs.parm.ast.ParmNode.*;
import static toylangs.parm.ParmParser.*;

public class ParmAst {


    public static ParmNode makeParmAst(String sisend) {
        ParmLexer lexer = new ParmLexer(CharStreams.fromString(sisend));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        ParmParser parser = new ParmParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        //System.out.println(tree.toStringTree(parser));
        return parseTreetoAst(tree);
    }

    // Implementeerida parsepuu -> AST teisendus!
    private static ParmNode parseTreetoAst(ParseTree tree) {
        throw new UnsupportedOperationException();
    }


    public static void main(String[] args) throws IOException {
        String input = "Kala <- 10; Koer <- Kass <- 5; Kala + Koer";
        ParmNode ast = makeParmAst(input);
        ast.renderPngFile(Paths.get("graphs", "parm.png"));
        System.out.println(ast);
    }

}
