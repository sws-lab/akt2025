package week1.soojendus;

import org.junit.Test;

import static org.junit.Assert.*;

public class NodeTest {

    public static String lastTestDescription = "";

    private static Node<Integer> n(int value, Node<Integer> left, Node<Integer> right) {
        return new Node<>(value, left, right);
    }

    // (3, _, _)
    private final Node<Integer> singleNodeTree = n(3, null, null);

    // (1, (2, _, (3, _, _)), (3, _, _))
    private final Node<Integer> sampleTree = n(1, n(2, null, n(3, null, null)), n(3, null, null));

    // (1000, (-2, _, (666, _, _)), (42, _, _))
    private final Node<Integer> biggerTree = n(1000, n(-2, null, n(666, null, null)), n(42, null, null));

    @Test
    public void testNodeIsLeaf() {
        String treeString = "(1, (2, _, (13, _, _)), (13, _, _))";
        lastTestDescription = treeString;

        Node<Integer> tree = n(1, n(2, null, n(13, null, null)), n(13, null, null));

        assertFalse(tree.isLeaf());
        assertFalse(tree.getLeftChild().isLeaf());
        assertTrue(tree.getRightChild().isLeaf());
        assertTrue(tree.getLeftChild().getRightChild().isLeaf());
    }

    @Test
    public void testNodeContains() {
        String treeString = "(1, (2, _, (13, _, _)), (13, _, _))";
        lastTestDescription = treeString;

        Node<Integer> tree = n(1, n(2, null, n(13, null, null)), n(13, null, null));
        assertTrue(tree.contains(13));
        assertFalse(tree.contains(3));
    }

    @Test
    public void testNodeToString() {
        String treeString = "(1, (2, _, (13, _, _)), (13, _, _))";
        lastTestDescription = treeString;

        Node<Integer> tree = n(1, n(2, null, n(13, null, null)), n(13, null, null));
        assertEquals("(1, (2, _, (13, _, _)), (13, _, _))", tree.toString());
    }

    @Test
    public void testCount() {
        testCount(singleNodeTree, 77, 0);
        testCount(singleNodeTree, 3, 1);
        testCount(sampleTree, 1, 1);
        testCount(sampleTree, 2, 1);
        testCount(sampleTree, 3, 2);
    }

    private void testCount(Node<Integer> tree, Integer value, int expectedResult) {
        lastTestDescription = "NodeUtils.count(<<" + tree + ">>, "+value+"))";
        int actualResult = NodeUtils.count(tree, value);
        if (actualResult != expectedResult) {
            fail("Sain tulemuseks "
                    + actualResult + ", aga oleks pidanud saama " + expectedResult);
        }
    }

    @Test
    public void testParseIntegerTree1() {
        testNodeToString();
        testParser(sampleTree);
        testParser(singleNodeTree);
    }

    @Test
    public void testParseIntegerTree2() {
        testNodeToString();
        testParser(biggerTree);
    }

    private void testParser(Node<Integer> tree) {
        lastTestDescription = "NodeUtils.parseIntegerTree(<<" + tree + ">>)";
        assertEquals(tree.toString(), NodeUtils.parseIntegerTree(tree.toString()).toString());
    }
}
