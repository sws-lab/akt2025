package week7.harjutused

import org.antlr.v4.runtime.BailErrorStrategy
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.Assert.fail
import org.junit.Test
import utils.ExceptionErrorListener

class SimpleArgListGrammarTest {
    @Test
    fun `legaalsed sisendid`() {
        legal("f()")
        legal("f(w)")
        legal("f(w,w)")
        legal("f(w,w,w)")
        for (count in (4..20)) {
            val args = (1..count).map { 'w' }.joinToString(",")
            legal("f($args)")
        }
    }

    @Test
    fun `legaalsed sisendid whitespaced`() {
        legal(" f        (                      )    ")
        legal("  f              (        w                  )   ")
        legal("   f ( w  , w )  ")
        legal("    f (w,w,w) ")
    }

    @Test
    fun `illegaalsed sisendid`() {
        illegal("f")
        illegal("f(")
        illegal("f)")
        illegal("()")
        illegal("x()")
        illegal("fw")
        illegal("(w)")
        illegal("f(x)")
        illegal("f(w,)")
        illegal("f(w,w,)")
        illegal("f(w;w)")
        illegal("x")
    }

    private fun legal(input: String) {
        parse(input)
    }

    private fun illegal(input: String) {
        runCatching { parse(input) }
            .onSuccess { fail("expected parse error: $input") }
    }

    private fun parse(input: String) {
        val lexer = SimpleArgListLexer(CharStreams.fromString(input)).apply {
            removeErrorListeners()
            addErrorListener(ExceptionErrorListener())
        }

        val parser = SimpleArgListParser(CommonTokenStream(lexer)).apply {
            removeErrorListeners()
            errorHandler = BailErrorStrategy()
        }

        parser.init()
    }
}
