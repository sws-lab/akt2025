package week7.harjutused

import org.antlr.v4.runtime.BailErrorStrategy
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.Assert.fail
import org.junit.Test
import utils.ExceptionErrorListener

class SimpleBinaryTreeAltGrammarTest {
    @Test
    fun `legaalsed sisendid`() {
        legal("null")

        legal("Node(null,0,null)")
        legal("Node(null,1,null)")

        legal("Node(Node(null,1,null),0,null)")
        legal("Node(null,0,Node(null,1,null))")
        legal("Node(Node(null,1,null),0,Node(null,1,null))")
    }

    @Test
    fun `legaalsed sisendid whitespaced`() {
        legal("   null  ")

        legal(" Node  (  null  ,     0 ,  null   ) ")
        legal("  Node     ( null    , 1  , null   )  ")
    }

    @Test
    fun `illegaalsed sisendid`() {
        illegal("0")
        illegal("1")
        illegal("()")
        illegal("(0)")
        illegal("(1)")

        illegal("Node")
        illegal("Node()")
        illegal("Node(0)")
        illegal("Node(null)")
        illegal("Node(null,1)")
        illegal("Node(1,null)")
        illegal("Node(0,1,null)")
        illegal("Node(null,1,null,)")
    }

    @Test
    fun `illegaalne whitespace`() {
        illegal("n ull")
        illegal("nu ll")
        illegal("nul l")

        illegal("N ode(null,0,null)")
        illegal("No de(null,1,null)")
        illegal("Nod e(null,0,null)")
    }

    private fun legal(input: String) {
        parse(input)
    }

    private fun illegal(input: String) {
        runCatching { parse(input) }
            .onSuccess { fail("expected parse error: $input") }
    }

    private fun parse(input: String) {
        val lexer = SimpleBinaryTreeAltLexer(CharStreams.fromString(input)).apply {
            removeErrorListeners()
            addErrorListener(ExceptionErrorListener())
        }

        val parser = SimpleBinaryTreeAltParser(CommonTokenStream(lexer)).apply {
            removeErrorListeners()
            errorHandler = BailErrorStrategy()
        }

        parser.init()
    }
}
