package week2.regexapi;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Exercise2Test {

    @Test
    public void test01() {
        runTest("def funktsioon(a,b):","funktsioon");
    }

    @Test
    public void test02() {
        runTest("def FUNKTSIOON(ESIMENE,TEINE): ... def teine(a,b): def koolonpuudu(yks,kaks)",
                "FUNKTSIOON",
                "teine");
    }

    @Test
    public void test03() {
        runTest("def     FUNKTSIOON   (   ESIMENE  ,  TEINE   ): ...... def _NiMi(EsI_mEnE,_): ... defmittesobiv(a,b): .. def kolmas(c,d",
                "FUNKTSIOON",
                "_NiMi");
    }

    @Test
    public void test04() {
        runTest("def fünktsioon1(  a1 ,  b1 ): .... def _(_A,_c1): ... def _FUN1(A1,B1): ",
                "fünktsioon1",
                "_",
                "_FUN1");
    }

    @Test
    public void test05() {
        runTest("def    _ä8888  (  _   ,   CCCC0000   )        : .... def Ä88(ÄÄ,öö): ... def poolik( ei, tohi ",
                "_ä8888",
                "Ä88");
    }

    private void runTest(String text, String... expectedNames){
        List<String> expected = Arrays.asList(expectedNames);
        List<String> actual = Exercise2.extractFunctions(text);
        assertEquals(expected,actual);
    }
}
