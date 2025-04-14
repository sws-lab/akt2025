package toylangs.bolog;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import toylangs.bolog.ast.BologNode;
import utils.ExceptionErrorListener;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static toylangs.bolog.ast.BologNode.*;
import static toylangs.bolog.BologParser.*;

public class BologAst {


    public static Set<BologNode> makeBologAst(String sisend) {
        BologLexer lexer = new BologLexer(CharStreams.fromString(sisend));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        BologParser parser = new BologParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.init();
        //System.out.println(tree.toStringTree(parser));
        return parseTreetoAst(tree);
    }

    // Implementeerida parsepuu -> AST teisendus!
    private static Set<BologNode> parseTreetoAst(ParseTree tree) {
        Set<BologNode> nodes = new HashSet<>();
        return nodes;
    }


    public static void main(String[] args) throws IOException {
        String input = "X kui Y\nZ && Y";
        Set<BologNode> bologNodes = makeBologAst(input);
        int idx = 1;
        for (BologNode bologNode : bologNodes) {
            bologNode.renderPngFile(Paths.get("graphs", "bolog" + idx++ + ".png"));
            System.out.println(bologNode);
        }
    }



}
