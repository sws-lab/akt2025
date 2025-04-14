package week7;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import utils.ExceptionErrorListener;
import week7.AktkParser.*;
import week7.ast.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class AktkAst {

    // Ise testimiseks soovitame kasutada selline meetod: inputs/sample.aktk failis sisend muuta.
    // Kui testid sinna kopeerida, siis äkki võtab IDE escape sümbolid ära ja on selgem,
    // milline see kood tegelikult välja näeb.
    public static void main(String[] args) throws IOException {
        String program = Files.readString(Paths.get("inputs", "sample.aktk"));
        AstNode ast = createAst(program);
        System.out.println(ast);
    }

    // Automaattestide jaoks vajalik meetod.
    public static AstNode createAst(String program) {
        AktkLexer lexer = new AktkLexer(CharStreams.fromString(program));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ExceptionErrorListener());

        AktkParser parser = new AktkParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        ParseTree tree = parser.program();
        //System.out.println(tree.toStringTree(parser));
        return parseTreeToAst(tree);
    }

    // Põhimeetod, mida tuleks implementeerida:
    private static AstNode parseTreeToAst(ParseTree tree) {
        throw new UnsupportedOperationException();
    }
}
