package io.gitlab.arturbosch.detekt.rules.formatting

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import com.natpryce.hamkrest.isEmpty
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.rules.format
import io.gitlab.arturbosch.detekt.rules.lint
import org.junit.jupiter.api.Test

/**
 * @author Shyiko
 */
class SpacingAroundCurlyBracesTest : RuleTest {

	override val rule: Rule = SpacingAroundCurlyBraces(Config.empty)

	@Test
	fun testLint() {
		assertThat(rule.lint("fun emit() { }"), isEmpty)
		assertThat(rule.lint("fun emit() {}"), isEmpty)
		assertThat(rule.lint("fun main() { val v = if (true) { return 0 } }"), isEmpty)
		assertThat(rule.lint("fun main() { fn({ a -> a }, 0) }"), isEmpty)
		assertThat(rule.lint("fun main() { fn({}, 0) && fn2({ }, 0) }"), isEmpty)
		assertThat(rule.lint("fun main() { find { it.default ?: false }?.phone }"), isEmpty)
		assertThat(rule.lint("fun main() { val v = if (true){return 0} }"), hasSize(equalTo(2)))
		assertThat(rule.lint("fun main() { fn({a -> a}, 0) }"), hasSize(equalTo(2)))
	}

	@Test
	fun testFormat() {
		assertThat(rule.format(
				"""
            fun main() {
                val v = if (true){return ""}
                val v = if (true) { return "" }
                fn({a -> a}, 0)
                fn({ a -> a }, 0)
                fn({},{}, {}, 0)
                fn({ }, 0)
                fn({ a -> try{a()}catch (e: Exception){null} }, 0)
                try{call()}catch (e: Exception){}
                call({}, {})
                a.let{}.apply({})
                f({ if (true) {r.add(v) }; r })
            }
            """
		), equalTo(
				"""
            fun main() {
                val v = if (true) { return "" }
                val v = if (true) { return "" }
                fn({ a -> a }, 0)
                fn({ a -> a }, 0)
                fn({}, {}, {}, 0)
                fn({ }, 0)
                fn({ a -> try { a() } catch (e: Exception) { null } }, 0)
                try { call() } catch (e: Exception) {}
                call({}, {})
                a.let {}.apply({})
                f({ if (true) { r.add(v) }; r })
            }
            """.trimIndent()//TODO #47
		))
	}
}