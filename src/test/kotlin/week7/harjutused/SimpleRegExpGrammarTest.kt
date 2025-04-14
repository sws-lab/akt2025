package week7.harjutused

import org.antlr.v4.runtime.BailErrorStrategy
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.Assert.fail
import org.junit.Test
import utils.ExceptionErrorListener

class SimpleRegExpGrammarTest {
    @Test
    fun `legaalsed sisendid`() {
        legal("c")
        legal("ac")
        legal("bc")
        legal("abc")
        legal("bac")
        legal("baac")
        legal("babac")
        legal("aaaaabbbbbbbbbbc")
        legal("bbbbbbbbaaaaabbbbbbaaaaaaaaaabbbbc")
    }

    @Test
    fun `illegaalsed sisendid`() {
        illegal("a")
        illegal("b")
        illegal("cc")
        illegal("ab")
        illegal("ba")
        illegal("x")

        illegal("a c")
        illegal("b c")
        illegal("a ac")
        illegal("a bc")
        illegal("b bc")
        illegal("b ac")
    }

    private fun legal(input: String) {
        parse(input)
    }

    private fun illegal(input: String) {
        runCatching { parse(input) }
            .onSuccess { fail("expected parse error: $input") }
    }

    private fun parse(input: String) {
        val lexer = SimpleRegExpLexer(CharStreams.fromString(input)).apply {
            removeErrorListeners()
            addErrorListener(ExceptionErrorListener())
        }

        val parser = SimpleRegExpParser(CommonTokenStream(lexer)).apply {
            removeErrorListeners()
            errorHandler = BailErrorStrategy()
        }

        parser.init()
    }
}
