package week6.parsers.xtra;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import week6.parsers.TestParser;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LittleParsersTest {

    private final TestParser<SAParser> saParser = new TestParser<>(SAParser.class);
    private final TestParser<AcdcParser> acdcParser = new TestParser<>(AcdcParser.class);
    private final TestParser<TypeParser> typeParser = new TestParser<>(TypeParser.class);

    @Test
    public void test01_SA1() {
        saParser.accepts("acb");
        saParser.accepts("abdb");
        saParser.accepts("ad");
        saParser.rejects("bdb");
        saParser.rejects("");
        saParser.rejects("a");
        saParser.rejects("b");
        saParser.rejects("d");
        saParser.accepts("aaaaacbbbbb");
        saParser.accepts("c");
        saParser.accepts("abbbbbbbdbbbbbbb");
        saParser.accepts("ad");
        saParser.rejects("bdb");
        saParser.rejects("abb");
        saParser.rejects("");
        saParser.rejects("a");
        saParser.rejects("b");
        saParser.rejects("d");
    }

    @Test
    public void test02_SA2() {
        saParser.accepts("aaaaacbbbbb");
        saParser.accepts("c");
        saParser.accepts("abbbbbbbdbbbbbbb");
        saParser.accepts("ad");
        saParser.accepts("abbbbbbbdbbbbbbbccccccc");
        saParser.accepts("abbbbbbbdcccccbbbbbbbccccccc");
        saParser.accepts("adccc");
        saParser.rejects("bdb");
        saParser.rejects("abb");
        saParser.rejects("");
        saParser.rejects("a");
        saParser.rejects("b");
        saParser.rejects("d");
        saParser.rejects("ac");
        saParser.rejects("ab");
    }

    @Test
    public void test03_SA3() {
        saParser.rejects("b", 0, "ac");
        saParser.rejects("az", 1, "abcd");
        saParser.rejects("aba", 2, "bd");
        saParser.rejects("adhd", 2, "c$", "ad");
        saParser.rejects("aadhd", 3, "cb", "ad");
    }


    @Test
    public void test04_SA4() {
        saParser.accepts("abdb", "S(a,R(A(b,A(d,Q(ε)),b,Q(ε))))");
        saParser.accepts("adccc", "S(a,R(A(d,Q(c,Q(c,Q(c,Q(ε)))))))");
    }

    @Test
    public void test05_acdc1() {
        acdcParser.accepts("acdc");
        acdcParser.accepts("aacdcdc");
        acdcParser.accepts("acaddc");
        acdcParser.accepts("acdcc");
        acdcParser.accepts("aaacddaaacdcdcdaaaadcdddacdcdadc");
        acdcParser.accepts("aaacddaaacdcdcdaaaadcdddacdcacdcdadc");
        acdcParser.accepts("aaacddaaacdcacaddcdcdaaaadcdddacdcadcdadc");
        acdcParser.rejects("a");
        acdcParser.rejects("ac");
        acdcParser.rejects("acdc  ");
        acdcParser.rejects("d");
        acdcParser.rejects("acadc");
        acdcParser.rejects("aaacddaaacdcdcdaaaadcddacdcdadc");
        acdcParser.rejects("aaacddaaacdcdcdaaadcdddacdcacdcdadc");
        acdcParser.rejects("aaacddaaacdcacaddcdcdaaaadcdddacdcadcadc");
        acdcParser.accepts("ac/dc");
        acdcParser.accepts("ac/dc");
        acdcParser.accepts("aaacddaaacdcacaddcdcdaaaadcdddacdcadcdadc");
        acdcParser.accepts("aaacddaaac//acdcdcac/addcdcdaaaadcdddacdcadcdadc");
        acdcParser.rejects("a");
        acdcParser.rejects("ac");
        acdcParser.rejects("acdc  ");
        acdcParser.rejects("d");
        acdcParser.rejects("aaacddaaacdcacaddcdcdaaaadcdddacdcadcadc");
        acdcParser.rejects("ac/d/c");
        acdcParser.rejects("aaacddaaac//acdcdca/c/addcdcdaaaadcdddacdcadcdadc");
    }

    @Test
    public void test06_acdc2() {
        acdcParser.rejects("g", 0, "ac$", "/");
        acdcParser.rejects("ag", 1, "acd", "/");
    }

    @Test
    public void test07_acdc3() {
        acdcParser.accepts("acdc", "S(a,S(C(c),S(ε)),d,S(C(c),S(ε)))");
        acdcParser.accepts("ac/dc", "S(a,S(C(C(c),/),S(ε)),d,S(C(c),S(ε)))");
        acdcParser.accepts("ac//dc", "S(a,S(C(C(C(c),/),/),S(ε)),d,S(C(c),S(ε)))");
    }

    @Test
    public void test08_Type1() {
        typeParser.accepts("int");
        typeParser.accepts("void");
        typeParser.rejects("hyperboloid");
        typeParser.rejects("");

        typeParser.accepts("int*");
        typeParser.accepts("void*");
        typeParser.rejects("*");
        typeParser.rejects("*int");

        typeParser.accepts("int**");
        typeParser.accepts("void*****");
        typeParser.rejects("**");
    }

    @Test
    public void test09_Type2() {
        typeParser.accepts("int()");
        typeParser.accepts("int(*)");
        typeParser.rejects("(int)");

        typeParser.accepts("int(())");
        typeParser.accepts("void*()");
        typeParser.rejects("int()()");
        typeParser.rejects("int(*)()");

        typeParser.accepts("int[]");
        typeParser.accepts("void[]");
        typeParser.rejects("[]");
        typeParser.rejects("[int]");
        typeParser.rejects("[]int");

        typeParser.accepts("int[][]");
        typeParser.accepts("void[][][][][][][]");
        typeParser.rejects("[][][]");
        typeParser.rejects("[int]");
        typeParser.rejects("int[[]]");

        typeParser.accepts("void([])");
        typeParser.accepts("void(*)[]");
        typeParser.rejects("void(*)([])");
        typeParser.accepts("int()[]");
        typeParser.rejects("int()([])");
        typeParser.accepts("void([])");
        typeParser.accepts("int(*)[]");
        typeParser.accepts("int(*[])[]");
        typeParser.accepts("int*[][]");
        typeParser.rejects("int*[]([])");
        typeParser.accepts("int*(*(*[])[])[]");
    }

    @Test
    public void test10_Type3() {
        typeParser.accepts("void[]", "TArray(TVoid)");
        typeParser.accepts("void(*)[]", "TPtr(TArray(TVoid))");
    }
}
