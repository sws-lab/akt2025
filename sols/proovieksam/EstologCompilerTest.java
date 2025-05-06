package proovieksam;

import cma.CMaInterpreter;
import cma.CMaProgram;
import cma.CMaStack;
import cma.CMaUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import proovieksam.ast.EstologDef;
import proovieksam.ast.EstologNode;
import proovieksam.ast.EstologProg;
import proovieksam.ast.aatomid.EstologLiteraal;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static proovieksam.ast.EstologNode.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EstologCompilerTest {

    private static final List<EstologDef> allDefs = Arrays.asList(
            def("x", lit(false)),
            def("y", lit(true)),
            def("z", lit(true)),
            def("a", lit(true)),
            def("b", lit(false)),
            def("c", lit(true))
    );

    @Test
    public void test01_literals() {
        checkCompile(prog(lit(false)), false);  // 0
        checkCompile(prog(lit(true)), true);  // 1
    }

    @Test
    public void test02_operators() {
        checkCompile(prog(ja(lit(false), lit(true))), false);  // (0 JA 1)
        checkCompile(prog(ja(lit(true), lit(true))), true);  // (1 JA 1)
        checkCompile(prog(voi(lit(false), lit(true))), true);  // (0 VOI 1)
        checkCompile(prog(voi(lit(false), lit(false))), false);  // (0 VOI 0)
        checkCompile(prog(vordus(lit(false), lit(true))), false);  // (0 = 1)
        checkCompile(prog(vordus(lit(false), lit(false))), true);  // (0 = 0)
    }

    @Test
    public void test03_variables() {
        checkCompile(prog(var("x"), def("x", lit(false))), false, false);  // x
        checkCompile(prog(var("y"), def("y", lit(true))), true, true);  // y
        checkCompile(prog(vordus(var("x"), lit(false)), def("x", lit(false))), false, true);  // (x = 0)
        checkCompile(prog(vordus(var("x"), var("c")), def("x", lit(false)), def("c", lit(true))), false, true, false);  // (x = c)

        // same with allDefs
        checkCompileAllDefsProg(var("x"), false);  // x
        checkCompileAllDefsProg(var("y"), true);  // y
        checkCompileAllDefsProg(vordus(var("x"), lit(false)), true);  // (x = 0)
        checkCompileAllDefsProg(vordus(var("x"), var("c")), false);  // (x = c)
    }

    @Test
    public void test04_kui() {
        checkCompileAllDefsProg(kui(lit(true), lit(true), lit(false)), true);  // (KUI 1 SIIS 1 MUIDU 0)
        checkCompileAllDefsProg(kui(lit(false), lit(true), lit(false)), false);  // (KUI 0 SIIS 1 MUIDU 0)
        checkCompileAllDefsProg(kui(lit(true), var("x"), var("z")), false);  // (KUI 1 SIIS x MUIDU z)

        // nested
        checkCompileAllDefsProg(kui(var("a"), kui(var("a"), lit(true), lit(false)), lit(false)), true);  // (KUI a SIIS (KUI a SIIS 1 MUIDU 0) MUIDU 0)
        checkCompileAllDefsProg(kui(var("a"), kui(var("b"), lit(false), lit(true)), lit(false)), true);  // (KUI a SIIS (KUI b SIIS 0 MUIDU 1) MUIDU 0)
        checkCompileAllDefsProg(kui(var("b"), kui(var("b"), lit(false), lit(false)), lit(true)), true);  // (KUI b SIIS (KUI b SIIS 0 MUIDU 0) MUIDU 1)
    }

    @Test
    public void test05_all() {
        checkCompileAllDefsProg(kui(var("b"), voi(var("x"), var("y")), ja(var("x"), lit(true))), false);  // (KUI b SIIS (x VOI y) MUIDU (x JA 1))
        checkCompileAllDefsProg(kui(var("b"), voi(var("x"), var("y")), ja(var("x"), var("y"))), false);  // (KUI b SIIS (x VOI y) MUIDU (x JA y))
        checkCompileAllDefsProg(kui(ja(var("b"), vordus(lit(false), lit(true))), voi(var("x"), var("y")), vordus(var("x"), var("y"))), false);  // (KUI (b JA (0 = 1)) SIIS (x VOI y) MUIDU (x = y))
        checkCompileAllDefsProg(voi(voi(ja(var("x"), var("y")), ja(var("a"), var("z"))), ja(var("b"), var("c"))), true);  // (((x JA y) VOI (a JA z)) VOI (b JA c))
        checkCompileAllDefsProg(voi(voi(ja(var("x"), var("y")), vordus(var("a"), var("z"))), ja(var("b"), var("c"))), true);  // (((x JA y) VOI (a = z)) VOI (b JA c))
    }

    @Test
    public void test06_defines() {
        // non-literal define
        checkCompile(prog(var("x"), def("x", vordus(lit(false), lit(false)))), true, true); // x := (0 = 0); x

        // dependent define
        checkCompile(prog(var("y"), def("x", lit(false)), def("y", vordus(var("x"), lit(false)))), false, true, true); // x := 0; y := (x = 0); y

        // redefine
        checkCompile(prog(var("x"), def("x", lit(true)), def("x", lit(false))), false, false); // x := 1; x := 0; x

        // dependent redefine
        checkCompile(prog(var("x"), def("x", lit(false)), def("x", vordus(var("x"), lit(false)))), true, true); // x := 0; x := (x = 0); x
    }

    @Test
    public void test07_kui_without_muidu() {
        checkCompile(prog(kui(lit(true), lit(false))), false);  // (KUI 1 SIIS 0)
        checkCompile(prog(kui(lit(false), lit(false))), true);  // (KUI 0 SIIS 0)
        checkCompileAllDefsProg(kui(lit(true), lit(false)), false);  // (KUI 1 SIIS 0)
        checkCompileAllDefsProg(kui(lit(false), lit(false)), true);  // (KUI 0 SIIS 0)
        checkCompileAllDefsProg(kui(lit(true), var("x")), false);  // (KUI 1 SIIS x)
        checkCompileAllDefsProg(kui(lit(false), var("x")), true);  // (KUI 0 SIIS x)

        checkCompileAllDefsProg(kui(var("b"), voi(lit(false), var("y"))), true);  // (KUI b SIIS (0 VOI y))
        checkCompileAllDefsProg(kui(var("b"), voi(var("x"), var("y"))), true);  // (KUI b SIIS (x VOI y))
    }

    @Test(expected = NoSuchElementException.class)
    public void test08_variable_undefined() {
        EstologCompiler.compile(prog(var("y"), def("x", lit(true)))); // x := 1; y
    }

    private void checkCompile(EstologProg prog, Boolean... expected) {
        CMaProgram program = EstologCompiler.compile(prog);
        CMaStack initialStack = new CMaStack();
        CMaStack finalStack = CMaInterpreter.run(program, initialStack);
        CMaStack expectedStack = new CMaStack(Arrays.stream(expected)
                .map(CMaUtils::bool2int)
                .collect(Collectors.toList()));
        assertEquals(expectedStack, finalStack);
    }

    private void checkCompileAllDefsProg(EstologNode node, boolean expected) {
        EstologProg prog = prog(node, allDefs);
        Boolean[] expectedAllDefs = Stream.concat(
                        allDefs.stream()
                                .map(def -> ((EstologLiteraal) def.getAvaldis()).getValue()),
                        Stream.of(expected))
                .toArray(Boolean[]::new);
        checkCompile(prog, expectedAllDefs);
    }
}
