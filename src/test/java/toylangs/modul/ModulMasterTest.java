package toylangs.modul;

import cma.CMaProgram;
import toylangs.modul.ast.ModulProg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.List;
import java.util.Map;

import static toylangs.modul.ast.ModulNode.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ModulMasterTest.EvaluatorTest.class,
        ModulMasterTest.CompilerTest.class,
})
public class ModulMasterTest {

    public static class EvaluatorTest extends ModulEvaluatorTest {

        @Test(timeout = 1000)
        public void test11_pow_extra() {
            checkEval(prog(pow(num(2), 1_000_000), 1009), 384);
            checkEval(prog(pow(num(2), 10_000_000), 1009), 28);
            checkEval(prog(pow(num(2), 100_000_000), 1009), 845);
            checkEval(prog(pow(num(2), 1_000_000_000), 1009), 942);
        }

        @Override
        protected int eval(ModulProg prog, Map<String, Integer> env) {
            return ModulMaster.eval(prog, env);
        }
    }

    public static class CompilerTest extends ModulCompilerTest {

        @Test(timeout = 1000)
        public void test11_pow_extra() {
            checkCompile(prog(pow(num(2), 1_000_000), 1009), 384);
            checkCompile(prog(pow(num(2), 10_000_000), 1009), 28);
            checkCompile(prog(pow(num(2), 100_000_000), 1009), 845);
            checkCompile(prog(pow(num(2), 1_000_000_000), 1009), 942);
        }

        @Override
        protected CMaProgram compile(ModulProg prog, List<String> variables) {
            return ModulMaster.compile(prog, variables);
        }
    }
}
