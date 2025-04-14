package week10;

import week9.AktkBinding;
import week7.AktkAst;
import week7.ast.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.objectweb.asm.Opcodes.*;


public class AktkCompiler {

    public static void main(String[] args) throws IOException {
        // lihtsam viis "käsurea parameetrite andmiseks":
        //args = new String[] {"inputs/yks_pluss_yks.aktk"};

        if (args.length != 1) {
            throw new IllegalArgumentException("Sellele programmile tuleb anda parameetriks kompileeritava AKTK faili nimi");
        }

        Path sourceFile = Paths.get(args[0]);
        if (!Files.isRegularFile(sourceFile)) {
            throw new IllegalArgumentException("Ei leia faili nimega '" + sourceFile + "'");
        }

        String className = sourceFile.getFileName().toString().replace(".aktk", "");
        Path classFile = sourceFile.toAbsolutePath().getParent().resolve(className + ".class");

        createClassFile(sourceFile, className, classFile);
    }

    private static void createClassFile(Path sourceFile, String className, Path classFile) throws IOException {
        // loen faili sisu muutujasse
        String source = Files.readString(sourceFile);

        // parsin ja moodustan AST'i
        AstNode ast = AktkAst.createAst(source);

        // seon muutujad
        AktkBinding.bind(ast);

        // kompileerin
        byte[] bytes = createClass(ast, className);
        Files.write(classFile, bytes);
    }

    public static byte[] createClass(AstNode ast, String className) {
        throw new UnsupportedOperationException();
    }
}
