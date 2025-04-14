package week7.harjutused

import org.antlr.v4.runtime.BailErrorStrategy
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.Assert.fail
import org.junit.Test
import utils.ExceptionErrorListener

class SimpleBinaryTreeGrammarTest {
    @Test
    fun `legaalsed sisendid`() {
        legal("Leaf(0)")
        legal("Leaf(1)")

        legal("Node(Leaf(0),Leaf(1))")
        legal("Node(Node(Leaf(0),Leaf(0)),Leaf(1))")
    }

    @Test
    fun `legaalsed sisendid whitespaced`() {
        legal("  Leaf  ( 0          )     ")
        legal("    Leaf        (    1   )  ")

        legal(" Node  (        Leaf  (     0 )        ,     Leaf(1) )    ")
        legal("  Node   (  Node (   Leaf ( 0    ) ,   Leaf(0    ))      , Leaf  ( 1)  )  ")
    }

    @Test
    fun `illegaalsed sisendid`() {
        illegal("0")
        illegal("1")
        illegal("()")
        illegal("(0)")
        illegal("(1)")

        illegal("Leaf")
        illegal("Leaf()")

        illegal("Node")
        illegal("Node()")
        illegal("Node(Leaf(0))")
    }

    @Test
    fun `illegaalne whitespace`() {
        illegal("L eaf(0)")
        illegal("Le af(1)")
        illegal("Lea f(0)")

        illegal("N ode(Leaf(0),Leaf(1))")
        illegal("No de(Leaf(0),Leaf(1))")
        illegal("Nod e(Leaf(0),Leaf(1))")
    }

    private fun legal(input: String) {
        parse(input)
    }

    private fun illegal(input: String) {
        runCatching { parse(input) }
            .onSuccess { fail("expected parse error: $input") }
    }

    private fun parse(input: String) {
        val lexer = SimpleBinaryTreeLexer(CharStreams.fromString(input)).apply {
            removeErrorListeners()
            addErrorListener(ExceptionErrorListener())
        }

        val parser = SimpleBinaryTreeParser(CommonTokenStream(lexer)).apply {
            removeErrorListeners()
            errorHandler = BailErrorStrategy()
        }

        parser.init()
    }
}
