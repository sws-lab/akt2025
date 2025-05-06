package week8;

import week7.AktkAst;
import week7.ast.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AktkInterpreter {

    public static void run(String program) {
        AstNode root = AktkAst.createAst(program);
        new AktkInterpeterVisitor().visit(root);
    }

    // Avaldise väärtus võib olla nii täisarv (Integer) kui ka String
    // seega kasutame visitori tagastustüübina üldist Object tüüpi,
    // lausete korral tagastame null.
    private static class AktkInterpeterVisitor extends AstVisitor<Object> {

        private final Environment<Object> env = new Environment<>();

        // Spetsiaalne muutujanimi, mida kasutame funktsiooni tagastusväärtuse hoidmiseks keskkonnas
        private static final String FUNCTION_RETURN_NAME = "__return__";

        @Override
        protected Object visit(Assignment assignment) {
            String name = assignment.getVariableName();
            Object value = visit(assignment.getExpression());
            env.assign(name, value);

            return null;
        }

        @Override
        protected Object visit(Block block) {
            env.enterBlock();
            for (Statement stmt : block.getStatements()) {
                visit(stmt);
            }
            env.exitBlock();

            return null;
        }

        @Override
        protected Object visit(ExpressionStatement expressionStatement) {
            // Eraldiseisval avaldisel on ka väärtus, aga ignoreerime seda
            visit(expressionStatement.getExpression());

            return null;
        }

        @Override
        protected Object visit(FunctionCall functionCall) {
            String name = functionCall.getFunctionName();

            List<Object> argValues = new ArrayList<>();
            for (Expression arg : functionCall.getArguments()) {
                argValues.add(visit(arg));
            }

            if (functionCall.isArithmeticOperation()) {
                return callArithmeticOperation(name, argValues);
            }
            else if (functionCall.isComparisonOperation()) {
                // Teisendame tõevääruse AKTK jaoks täisarvuks
                return callComparisonOperation(name, argValues) ? 1 : 0;
            }
            else if (env.get(name) != null) {
                return callAktkFunction((FunctionDefinition) env.get(name), argValues);
            }
            else {
                return callBuiltinFunction(name, argValues);
            }
        }

        private Object callArithmeticOperation(String name, List<Object> argValues) {
            switch (argValues.size()) {
                case 1: // unaarne operaator
                    Object value = argValues.getFirst();
                    if (value instanceof Integer iValue) {
                        return switch (name) {
                            case "-" -> -iValue;
                            default -> throw new UnsupportedOperationException("unknown unary operator called on integer");
                        };
                    }
                    else {
                        throw new UnsupportedOperationException("unary operator called on string");
                    }

                case 2: // binaarne operaator
                    Object left = argValues.get(0);
                    Object right = argValues.get(1);
                    if (left instanceof Integer iLeft && right instanceof Integer iRight) {
                        return switch (name) {
                            case "+" -> iLeft + iRight;
                            case "-" -> iLeft - iRight;
                            case "*" -> iLeft * iRight;
                            case "/" -> iLeft / iRight;
                            case "%" -> iLeft % iRight;
                            default -> throw new UnsupportedOperationException("unknown binary operator called on integers");
                        };
                    }
                    else if (left instanceof String sLeft && right instanceof String sRight) {
                        return switch (name) {
                            case "+" -> sLeft + sRight;
                            default -> throw new UnsupportedOperationException("unknown binary operator called on strings");
                        };
                    }
                    else {
                        throw new RuntimeException("binary operator called on mismatching types");
                    }

                default:
                    throw new UnsupportedOperationException("arithmetic operator called with invalid number of arguments");
            }
        }

        private boolean callComparisonOperation(String name, List<Object> argValues) {
            if (argValues.size() != 2) {
                throw new UnsupportedOperationException("comparison operator called with invalid number of arguments");
            }

            Object left = argValues.get(0);
            Object right = argValues.get(1);

            int compare;
            if (left instanceof Integer l && right instanceof Integer r) {
                compare = Integer.compare(l, r);
            }
            else if (left instanceof String l && right instanceof String r) {
                compare = l.compareTo(r);
            }
            else {
                throw new RuntimeException("comparison operator called on mismatching types");
            }

            return switch (name) {
                case "==" -> compare == 0;
                case "!=" -> compare != 0;
                case "<" -> compare < 0;
                case "<=" -> compare <= 0;
                case ">" -> compare > 0;
                case ">=" -> compare >= 0;
                default -> throw new UnsupportedOperationException("unknown comparison operator called");
            };
        }

        private Object callAktkFunction(FunctionDefinition functionDefinition, List<Object> argValues) {
            env.enterBlock();

            if (argValues.size() != functionDefinition.getParameters().size()) {
                throw new RuntimeException("function called with invalid number of arguments");
            }
            for (int i = 0; i < argValues.size(); i++) {
                env.declareAssign(functionDefinition.getParameters().get(i).getVariableName(), argValues.get(i));
            }
            env.declare(FUNCTION_RETURN_NAME);

            visit(functionDefinition.getBody());

            Object returnValue = env.get(FUNCTION_RETURN_NAME);
            env.exitBlock();
            return returnValue;
        }

        private Object callBuiltinFunction(String name, List<Object> argValues) {
            // Leia argumendite tüübid, et reflection õige meetodi üles leiaks
            Class<?>[] argClasses = new Class[argValues.size()];
            for (int i = 0; i < argValues.size(); i++) {
                argClasses[i] = argValues.get(i).getClass();
            }

            try {
                Method method = AktkInterpreterBuiltins.class.getDeclaredMethod(name, argClasses);
                return method.invoke(null, argValues.toArray());
            }
            catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected Object visit(FunctionDefinition functionDefinition) {
            env.declareAssign(functionDefinition.getName(), functionDefinition);

            return null;
        }

        @Override
        protected Object visit(IfStatement ifStatement) {
            if (isAktkTrue(visit(ifStatement.getCondition()))) {
                visit(ifStatement.getThenBranch());
            }
            else {
                visit(ifStatement.getElseBranch());
            }

            return null;
        }

        @Override
        protected Object visit(IntegerLiteral integerLiteral) {
            return integerLiteral.getValue();
        }

        @Override
        protected Object visit(ReturnStatement returnStatement) {
            Object value = visit(returnStatement.getExpression());
            env.assign(FUNCTION_RETURN_NAME, value);

            return null;
        }

        @Override
        protected Object visit(StringLiteral stringLiteral) {
            return stringLiteral.getValue();
        }

        @Override
        protected Object visit(Variable variable) {
            return env.get(variable.getName());
        }

        @Override
        protected Object visit(VariableDeclaration variableDeclaration) {
            String name = variableDeclaration.getVariableName();
            Object value = null;
            if (variableDeclaration.getInitializer() != null) {
                value = visit(variableDeclaration.getInitializer());
            }
            env.declareAssign(name, value);

            return null;
        }

        @Override
        protected Object visit(WhileStatement whileStatement) {
            while (isAktkTrue(visit(whileStatement.getCondition()))) {
                visit(whileStatement.getBody());
            }

            return null;
        }
    }

    private static boolean isAktkTrue(Object object) {
        return object instanceof Integer i && i != 0;
    }
}
