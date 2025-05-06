package week9;

import week7.ast.*;
import week8.AktkInterpreterBuiltins;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class AktkTypeChecker {

    public static void check(AstNode ast) {
        // Meetod peab viskama RuntimeException-i (või mõne selle alamklassi erindi), kui:
        // 1) programmis kasutatakse deklareerimata muutujaid või funktsioone,
        // mida pole defineeritud ei antud programmis ega "standardteegis"
        //    (vt. interpretaatori koduülesannet)
        // 2) programmis kasutatakse mõnd lihttüüpi, mis pole ei String ega Integer
        // 3) leidub muutuja deklaratsioon, milles pole antud ei tüüpi ega algväärtusavaldist
        // 4) programmis on mõnel avaldisel vale tüüp
        AktkBinding.bind(ast);
        new AktkTypeCheckerVisitor().visit(ast);
    }

    private static final String INTEGER_TYPE = "Integer";
    private static final String STRING_TYPE = "String";

    private static class AktkTypeCheckerVisitor extends AstVisitor<String> {
        @Override
        protected String visit(Assignment assignment) {
            if (assignment.getBinding() == null) {
                throw new RuntimeException("unbound variable '%s'".formatted(assignment.getVariableName()));
            }

            String variableType = assignment.getBinding().getType();
            String expressionType = visit(assignment.getExpression());
            if (!variableType.equals(expressionType)) {
                throw new RuntimeException("assignment expression type differs from variable type");
            }

            return null;
        }

        @Override
        protected String visit(Block block) {
            for (Statement statement : block.getStatements()) {
                visit(statement);
            }

            return null;
        }

        @Override
        protected String visit(ExpressionStatement expressionStatement) {
            // Eraldiseisval avaldisel on ka tüüp, kontrollime, aga ignoreerime seda
            visit(expressionStatement.getExpression());

            return null;
        }

        @Override
        protected String visit(FunctionCall functionCall) {
            String name = functionCall.getFunctionName();
            List<String> argTypes = new ArrayList<>();
            for (Expression expression : functionCall.getArguments()) {
                String visit = visit(expression);
                argTypes.add(visit);
            }

            switch (name) {
                case "+":
                    if (argTypes.get(0).equals(argTypes.get(1))) return argTypes.get(0);
                    else throw new IllegalArgumentException("Plus types must match!");
                case "==":
                case "!=":
                    if (argTypes.get(0).equals(argTypes.get(1))) return INTEGER_TYPE;
                    else throw new IllegalArgumentException("Equality types must match!");
                default:
                    if (functionCall.isArithmeticOperation() || functionCall.isComparisonOperation()) {
                        for (String t : argTypes) {
                            if (!t.equals(INTEGER_TYPE)) {
                                throw new IllegalArgumentException("Operation needs integer arguments!");
                            }
                        }
                        return INTEGER_TYPE;
                    }
                    FunctionDefinition functionBinding = functionCall.getFunctionBinding();
                    if (functionBinding != null) return checkUserDefinedFunction(argTypes, functionBinding);
                    return checkBuiltinFunction(name, argTypes);
            }
        }

        private String checkUserDefinedFunction(List<String> types, FunctionDefinition functionBinding) {
            List<String> expected = new ArrayList<>();
            for (FunctionParameter functionParameter : functionBinding.getParameters()) {
                expected.add(functionParameter.getType());
            }
            if (!expected.equals(types)) throw new IllegalArgumentException("Type error");
            return functionBinding.getReturnType();
        }


        private String checkBuiltinFunction(String name, List<String> argTypes) {
            // Leia argumendite tüüpide klassid, et reflection õige meetodi üles leiaks
            Class<?>[] argClasses = new Class[argTypes.size()];
            for (int i = 0; i < argTypes.size(); i++) {
                argClasses[i] = getClassForType(argTypes.get(i));
            }

            try {
                Method method = AktkInterpreterBuiltins.class.getDeclaredMethod(name, argClasses);
                return getTypeForClass(method.getReturnType());
            }
            catch (NoSuchMethodException e) {
                throw new RuntimeException("unknown builtin function", e);
            }
        }

        @Override
        protected String visit(FunctionDefinition functionDefinition) {
            // Parameetrite tüübid peavad olema toetatud
            for (FunctionParameter parameter : functionDefinition.getParameters()) {
                if (!isValidType(parameter.getType())) {
                    throw new RuntimeException("unknown parameter type");
                }
            }
            // Tagastustüüp peab olema toetatud, kui see on olemas
            String returnType = functionDefinition.getReturnType();
            if (returnType != null && !isValidType(returnType)) {
                throw new RuntimeException("unknown return type");
            }

            visit(functionDefinition.getBody());

            return null;
        }

        @Override
        protected String visit(IfStatement ifStatement) {
            String conditionType = visit(ifStatement.getCondition());
            if (!conditionType.equals(INTEGER_TYPE)) {
                throw new RuntimeException("invalid condition type");
            }
            visit(ifStatement.getThenBranch());
            visit(ifStatement.getElseBranch());

            return null;
        }

        @Override
        protected String visit(IntegerLiteral integerLiteral) {
            return INTEGER_TYPE;
        }

        @Override
        protected String visit(ReturnStatement returnStatement) {
            String expressionType = visit(returnStatement.getExpression());
            String returnType = returnStatement.getFunctionBinding().getReturnType();
            if (!expressionType.equals(returnType)) {
                throw new RuntimeException("return expression type differs from function return type");
            }

            return null;
        }

        @Override
        protected String visit(StringLiteral stringLiteral) {
            return STRING_TYPE;
        }

        @Override
        protected String visit(Variable variable) {
            if (variable.getBinding() == null) {
                throw new RuntimeException("unbound variable '%s'".formatted(variable.getName()));
            }

            return variable.getBinding().getType();
        }

        @Override
        protected String visit(VariableDeclaration variableDeclaration) {
            // Muutuja deklaratsioonis peab olema kas tüüp või algväärtusavaldis või mõlemad.
            // Kui on mõlemad, siis nende tüübid peavad klappima.

            // Algväärtusavaldis ei näe seda muutujat, mida parasjagu deklareeritakse
            // st. lause var x = x + 1 omab mõtet vaid siis, kui ka mõnes ülevalpool olevas skoobis
            // on deklareeritud muutuja x.

            String declarationType = variableDeclaration.getType();
            if (variableDeclaration.getInitializer() != null) {
                String initializerType = visit(variableDeclaration.getInitializer());
                if (declarationType != null) {
                    if (!declarationType.equals(initializerType)) {
                        throw new RuntimeException("variable declaration type differs from initializer expression type");
                    }
                }
                else {
                    variableDeclaration.setType(initializerType);
                }
            }
            else {
                if (declarationType == null) {
                    throw new RuntimeException("missing variable type");
                }
            }

            // Muutuja tüüp (deklareeritud või tuletatud) peab olema toetatud
            if (!isValidType(variableDeclaration.getType())) {
                throw new RuntimeException("unknown variable type");
            }

            return null;
        }

        @Override
        protected String visit(WhileStatement whileStatement) {
            String conditionType = visit(whileStatement.getCondition());
            if (!conditionType.equals(INTEGER_TYPE)) {
                throw new RuntimeException("invalid condition type");
            }
            visit(whileStatement.getBody());

            return null;
        }
    }

    private static boolean isValidType(String type) {
        return switch (type) {
            case INTEGER_TYPE, STRING_TYPE -> true;
            default -> false;
        };
    }

    private static String getTypeForClass(Class<?> clazz) {
        if (clazz == Integer.class) {
            return INTEGER_TYPE;
        }
        else if (clazz == String.class) {
            return STRING_TYPE;
        }
        else if (clazz == void.class) {
            return null;
        }
        else {
            throw new UnsupportedOperationException("unsupported Java type " + clazz);
        }
    }

    private static Class<?> getClassForType(String type) {
        return switch (type) {
            case INTEGER_TYPE -> Integer.class;
            case STRING_TYPE -> String.class;
            default -> throw new UnsupportedOperationException("unsupported AKTK type " + type);
        };
    }
}
