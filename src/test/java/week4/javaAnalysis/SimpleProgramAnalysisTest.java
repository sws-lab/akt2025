package week4.javaAnalysis;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.fail;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimpleProgramAnalysisTest {
    public static String lastTestDescription = "";

    @Test
    public void oneLetterNames1() {
        testOneLetterNames("""
                public class Demo1 {\r
                    public static int f (int x, int y) {\r
                        int z = x + y;\r
                        return z;\r
                    }\r
                 /* xx ss z */}""", true);

        testOneLetterNames("""
                public class Demo1 {\r
                    public static int fun (int xx, int yx) {\r
                        int zr = xx + yx;\r
                        String ss = "a b c d";\r
                        return zr; // x y z\r
                    }/* x y z xx yy zz zr */\r
                }""", false);
    }

    @Test
    public void oneLetterNames2() {
        testOneLetterNames("""
                public class NestedLoops1 {\r
                    void f() {\r
                        while (true) {\r
                            int[] a = new int[10];\r
                            \r
                            for (int x : a) {\r
                                \r
                                for (int j=0; j < 10; j++) {\r
                                    \r
                                }\r
                            }\r
                        }\r
                    }/* x y z xx yy zz zr */\r
                }""", true);

        testOneLetterNames("""
                public class Demo1 {\r
                    public static int fun (int xx, int yx) {\r
                        int zr = xx + yx;\r
                        String ss = "a b c d";\r
                        return zr;\r
                    }\r
                 /* x y z xx yy zz zr */}""", false);
    }

    @Test
    public void oneLetterNames3() {
        testOneLetterNames("""
                public class Demo1 {\r
                    public static int frr (int xxx, int y) {\r
                        int zww = xxx + y;\r
                        return zww;\r
                    }\r
                }""", true);

        testOneLetterNames("public class CLaSS { }", false);
    }


    @Test
    public void oneLetterNames4() {
        testOneLetterNames("""
                public class Demo1 {\r
                    public static int f (int xqq, int yqq) {\r
                        int zqq = xqq + yqq;\r
                        return zqq;\r
                    }/* x y z xx yy zz zr */\r
                }""", true);

        testOneLetterNames("public class CLaSS { }/* x y z xx yy zz zr */", false);
    }

    @Test
    public void oneLetterNames5() {
        testOneLetterNames("public class C { }/* x y z xx yy zz zr */", true);

        testOneLetterNames("""
                public class Demo1 {\r
                    public static int fun (int xx, int yx) {\r
                        int zr = xx + yx;\r
                        String ss = "a b c d";\r
                        return zr;\r
                    }/* x y z xx yy zz zr */\r
                }""", false);

        testOneLetterNames("""
                public class Demo1 {\r
                    public static int ff (int xx, int yy) {\r
                        int zz = xx + yy;\r
                        return zz;\r
                    }\r
                    public static int ff (int xx, int yy) {\r
                        int z = xx + yy;\r
                        return z;\r
                    }/* x y z xx yy zz zr */\r
                }""", true);

        testOneLetterNames("""
                public class Demo1 {\r
                    public static int fun (int xx, int yx) {\r
                        String ss = "a b cc c d";\r
                        return 3;\r
                    }/* x y z xx yy zz zr */\r
                }""", false);
    }

    @Test
    public void testElses1() {
        testElses("public class C { }/* else if else */", 0);
        testElses("public class C { void f() {} }/* else if else */", 0);
        testElses("""
                public class abc {\r
                    void f() {\r
                        if (true) {\r
                            System.out.println("jah");\r
                        } else if (false || true) {\r
                            System.out.println("ei");\r
                        }\r
                    }/* else if else */\r
                }\r
                """, 1);
    }

    @Test
    public void testElses2() {
        testElses("public class C { }/* else if else */", 0);
        testElses("public class C { void f() {} }/* else if else */", 0);
        testElses("""
                public class abc {\r
                    void f() {\r
                        if (true) {\r
                            System.out.println("if (kala) {} else { else }");\r
                        } else {\r
                            System.out.println();\r
                        }\r
                    }\r
                }/* else if else */\r
                """, 1);

        testElses("""
                public class abc {\r
                    void f() {\r
                        if (true) {\r
                            System.out.println("if (kala) {} else { else }");\r
                        }\r
                    }\r
                }/* else if else */\r
                """, 0);
    }

    @Test
    public void testNestedLoops1() {
        testNestedLoops("""
                public class SingleLoop {\r
                    void f() {\r
                        while (true) {\r
                            /* while */\r
                            String ss = " while for while ";\r
                        }\r
                    }\r
                }""", 0);

        testNestedLoops("""
                \r
                public class NestedLoops1 {\r
                    void f() {\r
                        while (true) {\r
                            int[] a = new int[10];\r
                            \r
                            for (int x : a) {\r
                                \r
                                for (int j=0; j < 10; j++) {\r
                                    /* while */\r
                                }\r
                            }\r
                        }\r
                    }\r
                }\r
                """, 2);
    }


    @Test
    public void testNestedLoops2() {
        testNestedLoops("""
                public class SingleLoop {\r
                    void f() {\r
                        while (true) {\r
                            \r
                        }\r
                        while (true) {\r
                            /* while */\r
                        }\r
                    }\r
                }""", 0);

        testNestedLoops("""
                \r
                public class NestedLoops1 {\r
                    void f(boolean cond) {\r
                        if (cond) {\r
                            while (cond) {\r
                                int[] a = new int[10];\r
                                \r
                                for (int x : a) {\r
                                    \r
                                    for (int j=0; j < 10; j++) {\r
                                        \r
                                    }\r
                                }\r
                            }\r
                            \r
                            while (true) {\r
                                int[] a = new int[10];\r
                                /* while */\r
                                for (int x : a) {\r
                                    \r
                                    for (int j=0; j < 10; j++) {\r
                                        \r
                                    }\r
                                }\r
                            }\r
                        }\r
                    }\r
                }\r
                \r
                """, 4);
    }


    @Test
    public void testUnusedVars1() {
        testUnusedVars("public class C { }", 0);

        testUnusedVars("""
                public class abc {\r
                    void def() {\r
                        int x = 3;\r
                        String y;\r
                        String ss = "x y z";\r
                    }\r
                }\r
                """, 3);

        testUnusedVars("""
                public class abc {\r
                    void def() {\r
                        int x = 3;\r
                        String y = "---";\r
                        System.out.println(x + " ja " + y);\r
                    }\r
                }\r
                """, 0);

        testUnusedVars("""
                public class abc {\r
                    void def() {\r
                        int x = 3;\r
                        String y;\r
                        String ss = y + "x y z";\r
                    }\r
                }\r
                """, 2);

    }


    private static void testOneLetterNames(String src, boolean expectedResult) {
        lastTestDescription = "Annan argumendiks sellise programmi:\n\n>"
                + src.replaceAll("\r\n", "\n>");

        if (SimpleProgramAnalysis.hasOneLetterNames(src) != expectedResult) {
            fail("Ootasin vastuseks " + expectedResult + ", aga sain " + !expectedResult);
        }
    }

    private static void testNestedLoops(String src, int expectedResult) {
        lastTestDescription = "Annan argumendiks sellise programmi:\n\n>"
                + src.replaceAll("\r\n", "\n>");

        int actualResult = SimpleProgramAnalysis.numberOfNestedLoops(src);
        if (actualResult != expectedResult) {
            fail("Ootasin vastuseks " + expectedResult + ", aga sain " + actualResult);
        }
    }

    private static void testUnusedVars(String src, int expectedResult) {
        lastTestDescription = "Annan argumendiks sellise programmi:\n\n>"
                + src.replaceAll("\r\n", "\n>");

        int actualResult = SimpleProgramAnalysis.numberOfUnusedVariables(src);
        if (actualResult != expectedResult) {
            fail("Ootasin vastuseks " + expectedResult + ", aga sain " + actualResult);
        }
    }

    private static void testElses(String src, int expectedResult) {
        lastTestDescription = "Annan argumendiks sellise programmi:\n\n>"
                + src.replaceAll("\r\n", "\n>");

        int actualResult = SimpleProgramAnalysis.numberOfElseKeywords(src);
        if (actualResult != expectedResult) {
            fail("Ootasin vastuseks " + expectedResult + ", aga sain " + actualResult);
        }
    }


}
