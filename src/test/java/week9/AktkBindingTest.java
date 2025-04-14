package week9;


import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;
import week7.AktkAst;
import week7.ast.AstNode;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AktkBindingTest {

    private AstInspector inspector;

    @Rule
    public TestRule globalTimeout = new DisableOnDebug(new Timeout(500, TimeUnit.MILLISECONDS));

    @Test
    public void test01_variables() {
        inspectBinding("var x = 3;\nprint(x)");
        assertVariableBoundDeclaration("x", 0, 0);

        inspectBinding("print(x);\nvar x = 3");
        assertVariableUnbound("x", 0);

        inspectBinding("print(y)");
        assertVariableUnbound("y", 0);

        inspectBinding("var x = 3;\nprint(y)");
        assertVariableUnbound("y", 0);

        // level1_incorrect_undefined
        inspectBinding("var x = y + 1");
        assertVariableUnbound("y", 0);

        // initializer
        inspectBinding("var x = 0;\nvar y = x");
        assertVariableBoundDeclaration("x", 0, 0);
    }

    @Test
    public void test02_simpleBlocks() {
        inspectBinding("var x = 3;\n{var x = 4; print(x)}");
        assertVariableBoundDeclaration("x", 0, 1);

        inspectBinding("var x = 3;\n{var y = 4; print(x)};\nprint(x);\nprint(y)");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableBoundDeclaration("x", 1, 0);
        assertVariableUnbound("y", 0);

        inspectBinding("var x = 3;\n{var x = 4; print(x)};\nprint(x);\nprint(y)");
        assertVariableBoundDeclaration("x", 0, 1);
        assertVariableBoundDeclaration("x", 1, 0);
        assertVariableUnbound("y", 0);
    }

    @Test
    public void test03_complexBlocks() {
        // level1_correct_differentBlock
        inspectBinding("""
                var x = readInt();

                if x / 2 == 0 then {
                    var d = 1;
                    printInt(x + 1)
                } else {
                    var d = 2;
                    printInt(x * d)
                }""");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableBoundDeclaration("x", 1, 0);
        assertVariableBoundDeclaration("x", 2, 0);
        assertVariableBoundDeclaration("d", 0, 1);

        // level1_incorrect_outsideBlock
        inspectBinding("""
                var x = readInt();

                if x / 2 == 0 then {
                    var y = 0;
                    printInt(x)
                } else {
                    var y = 1;
                    printInt(y)
                };

                printInt(y)""");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableBoundDeclaration("x", 1, 0);
        assertVariableBoundDeclaration("y", 0, 1);
        assertVariableUnbound("y", 1);
    }

    @Test
    public void test04_redefine() {
        // level1_correct_redefineBlock
        inspectBinding("""
                var x = readInt();

                if x / 2 == 0 then {
                    printInt(x)
                } else {
                    var x = 1;
                    printInt(x)
                };

                printInt(x)""");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableBoundDeclaration("x", 1, 0);
        assertVariableBoundDeclaration("x", 2, 1);
        assertVariableBoundDeclaration("x", 3, 0);

        // level1_correct_redefineOrder
        inspectBinding("""
                var x = 1;

                if x / 2 == 0 then {
                    printInt(x); /* kuvab ekraanile 1 */
                    var x = 2;   /* siit edasi, kuni ploki lõpuni tähendab x plokimuutujat */
                    printInt(x)  /* kuvab ekraanile 2 */
                } else {
                    pass()
                };

                printInt(x) /* kuvab ekraanile 1 */""");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableBoundDeclaration("x", 1, 0);
        assertVariableBoundDeclaration("x", 2, 1);
        assertVariableBoundDeclaration("x", 3, 0);
    }

    @Test
    public void test05_parameters() {
        // level2_correct_parameter
        inspectBinding("""
                var x = 45;

                fun printDouble(x:Integer) {
                    print(x);
                    print(x)
                }""");
        assertVariableBoundParameter("x", 0, 0);
        assertVariableBoundParameter("x", 1, 0);

        // level2_correct_outside
        inspectBinding("""
                var x = 45;

                fun printDouble(y:Integer) {
                    print(x);
                    print(x)
                }""");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableBoundDeclaration("x", 1, 0);
    }

    @Test
    public void test06_multipleFunctions() {
        inspectBinding("""
                var x = 45;

                fun pala(x:Integer) {
                    print(x)
                };

                fun kala() {
                    print(x)
                }""");
        assertVariableBoundParameter("x", 0, 0);
        assertVariableBoundDeclaration("x", 1, 0);

        inspectBinding("""
                var x = 45;

                fun pala(y:Integer) {
                    print(x)
                };

                fun kala(x:Integer) {
                    print(x)
                }""");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableBoundParameter("x", 1, 0);
    }

    @Test
    public void test07_functionBlocks() {
        inspectBinding("""
                var x = 45;

                fun printDouble(y:Integer) {
                    print(x);
                    var x = 3;
                    if kala > 3 then {
                        print(x)
                    } else { print(y)}
                }""");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableUnbound("kala", 0);
        assertVariableBoundDeclaration("x", 1, 1);
        assertVariableBoundParameter("y", 0, 0);

        inspectBinding("""
                var x = 45;

                fun printDouble(y:Integer) {
                    print(x);
                    var x = 3;
                    var y = 45;
                    if w > e then {
                        print(x)
                    } else {
                        print(y)
                    }
                }""");
        assertVariableBoundDeclaration("x", 0, 0);
        assertVariableUnbound("w", 0);
        assertVariableUnbound("e", 0);
        assertVariableBoundDeclaration("x", 1, 1);
        assertVariableBoundDeclaration("y", 0, 0);

    }

    @Test
    public void test08_assignments() {
        inspectBinding("var x = 3;\nx = 5");
        assertAssignmentBoundDeclaration("x", 0, 0);

        inspectBinding("x = 5;\nvar x = 3");
        assertAssignmentUnbound("x", 0);

        inspectBinding("y = 5");
        assertAssignmentUnbound("y", 0);

        inspectBinding("var x = 3;\ny = 5");
        assertAssignmentUnbound("y", 0);

        inspectBinding("var x = 3;\n{var x = 4; x = 5};\nx = 6");
        assertAssignmentBoundDeclaration("x", 0, 1);
        assertAssignmentBoundDeclaration("x", 1, 0);

        inspectBinding("""
                fun kala(x:Integer) {
                    x = 7;
                    print(x)
                }""");
        assertAssignmentBoundParameter("x", 0, 0);
    }

    @Test
    public void test09_functionCalls() {
        inspectBinding("""
                fun printDouble(x:Integer) {
                    print(x);
                    print(x)
                };

                printDouble(3)""");
        assertCallUnbound("print", 0);
        assertCallUnbound("print", 1);
        assertCallBoundDefinition("printDouble", 0, 0);
    }

    @Test
    public void test10_returns() {
        inspectBinding("""
                fun abs(x:Integer) {
                    if x < 0 then {
                        return -x
                    } else {
                        return x
                    }
                };

                return 42""");
        assertReturnBoundDefinition(0, "abs", 0);
        assertReturnBoundDefinition(1, "abs", 0);
        assertReturnUnbound(2);
    }

    private void inspectBinding(String program) {
        AstNode ast = AktkAst.createAst(program);
        inspector = new AstInspector(ast);
        AktkBinding.bind(ast);
    }

    // testimise abimeetodid...

    private void assertVariableBoundDeclaration(String name, int variableIndex, int declarationIndex) {
        assertSame("muutuja '%s' %d. kasutus pole seotud %d. deklaratsiooniga".formatted(name, variableIndex, declarationIndex),
                inspector.findVariableDeclaration(name, declarationIndex), inspector.findVariable(name, variableIndex).getBinding());
    }

    private void assertVariableBoundParameter(String name, int variableIndex, int parameterIndex) {
        assertSame("muutuja '%s' %d. kasutus pole seotud %d. parameetriga".formatted(name, variableIndex, parameterIndex),
                inspector.findFunctionParameter(name, parameterIndex), inspector.findVariable(name, variableIndex).getBinding());
    }

    private void assertVariableUnbound(String name, int index) {
        assertNull("muutuja '%s' %d. kasutus on seotud".formatted(name, index),
                inspector.findVariable(name, index).getBinding());
    }

    private void assertAssignmentBoundDeclaration(String variableName, int assignmentIndex, int declarationIndex) {
        assertSame("muutuja '%s' %d. omistamine pole seotud %d. deklaratsiooniga".formatted(variableName, assignmentIndex, declarationIndex),
                inspector.findVariableDeclaration(variableName, declarationIndex), inspector.findAssignment(variableName, assignmentIndex).getBinding());
    }

    private void assertAssignmentBoundParameter(String variableName, int assignmentIndex, int parameterIndex) {
        assertSame("muutuja '%s' %d. omistamine pole seotud %d. parameetriga".formatted(variableName, assignmentIndex, parameterIndex),
                inspector.findFunctionParameter(variableName, parameterIndex), inspector.findAssignment(variableName, assignmentIndex).getBinding());
    }

    private void assertAssignmentUnbound(String variableName, int index) {
        assertNull("muutuja '%s' %d. omistamine on seotud".formatted(variableName, index),
                inspector.findAssignment(variableName, index).getBinding());
    }

    private void assertCallBoundDefinition(String name, int callIndex, int definitionIndex) {
        assertSame("funktsiooni '%s' %d. kutse pole seotud %d. definitsiooniga".formatted(name, callIndex, definitionIndex),
                inspector.findFunctionDefinition(name, definitionIndex), inspector.findFunctionCall(name, callIndex).getFunctionBinding());
    }

    private void assertCallUnbound(String name, int index) {
        assertNull("funktsiooni '%s' %d. kutse on seotud".formatted(name, index),
                inspector.findFunctionCall(name, index).getFunctionBinding());
    }

    private void assertReturnBoundDefinition(int returnIndex, String functionName, int definitionIndex) {
        assertSame("%d. return pole seotud funktiooni '%s' %d. definitsiooniga".formatted(returnIndex, functionName, definitionIndex),
                inspector.findFunctionDefinition(functionName, definitionIndex), inspector.findReturn(returnIndex).getFunctionBinding());
    }

    private void assertReturnUnbound(int index) {
        assertNull("%d. return on seotud".formatted(index),
                inspector.findReturn(index).getFunctionBinding());
    }
}
