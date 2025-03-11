package week4.javaAnalysis;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class JavaASTUtils {

    public static CompilationUnit parseCompilationUnit(String source) {
        ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
        parser.setSource(source.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        return (CompilationUnit) parser.createAST(null);
    }

    public static List<ASTNode> getChildNodes(ASTNode node) {

        final List<ASTNode> result = new ArrayList<>();

        for (Object childDesc : node.structuralPropertiesForType()) {
            Object child = node.getStructuralProperty((StructuralPropertyDescriptor) childDesc);
            if (child instanceof ASTNode childNode) {
                result.add(childNode);
            }
            else if (child instanceof List<?> list) {
                for (Object item : list) {
                    if (item instanceof ASTNode itemNode) {
                        result.add(itemNode);
                    }
                }
            }
            else {
                // String or int or smth like that. Skip these
            }
        }

        return result;
    }
}
