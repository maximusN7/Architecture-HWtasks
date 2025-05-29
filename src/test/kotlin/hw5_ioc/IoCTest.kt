package hw5_ioc

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.example.hw5_ioc.ioc.IScope
import org.example.hw5_ioc.ioc.IoC
import org.example.hw5_ioc.ioc.Scope
import org.example.hw5_ioc.ioc.command.InitScopeBasedIoCCommand
import org.example.hw5_ioc.utils.ILambda
import org.example.hw5_ioc.utils.ResolveDependencyException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class IoCTest {

    // region IoC
    @Test
    fun WHEN_try_get_missing_command_with_default_strategy_EXPECT_get_exception() {
        val store: MutableMap<String, ILambda> = mutableMapOf(
            "a" to ILambda { _, _ -> 1 },
            "b" to ILambda { _, _ -> 2 }
        )
        val scope = Scope(store) { _, _ -> throw ResolveDependencyException() }

        assertEquals(1, scope.resolve("a"))
        assertEquals(2, scope.resolve("b"))
        assertThrows<ResolveDependencyException> { scope.resolve<Unit>("c") }
    }

    @Test
    fun WHEN_try_get_missing_command_with_custom_strategy_EXPECT_get_string() {
        val store: MutableMap<String, ILambda> = mutableMapOf(
            "a" to ILambda { _, _ -> 1 },
            "b" to ILambda { _, _ -> 2 }
        )
        val scope = Scope(store) { key, _ -> "$key not found" }

        assertEquals(1, scope.resolve("a"))
        assertEquals(2, scope.resolve("b"))
        assertEquals("c not found", scope.resolve("c"))
    }
    // endregion

    // region scoped based IoC
    @Test
    fun WHEN_try_get_storage_from_default_ioc_EXPECT_get_empty_map() {
        InitScopeBasedIoCCommand().invoke()

        assertEquals(mutableMapOf<String, ILambda>(), IoC.resolve("Scopes.Storage"))
    }

    @Test
    fun WHEN_try_get_missing_command_from_new_scope_with_default_strategy_EXPECT_get_exception() {
        InitScopeBasedIoCCommand().invoke()
        val scope = IoC.resolve("Scopes.New") as IScope

        assertThrows<ResolveDependencyException> { scope.resolve<Unit>("MISSING") }
    }

    @Test
    fun WHEN_try_get_missing_command_from_new_scope_with_custom_strategy_EXPECT_get_string() {
        InitScopeBasedIoCCommand().invoke()
        val scope = IoC.resolve(
            "Scopes.New",
            ILambda { key, _ -> "$key not found" },
        ) as IScope

        assertEquals("c not found", scope.resolve("c"))
    }

    @Test
    fun WHEN_try_get_new_scope_EXPECT_get_it() {
        InitScopeBasedIoCCommand().invoke()
        val current = IoC.resolve("Scopes.Current")
        val scope = IoC.resolve(
            "Scopes.New",
            current
        ) as IScope

        assertEquals(current, scope.resolve("Scopes.Current"))
    }

    @Test
    fun WHEN_try_get_registered_command_from_new_scope_EXPECT_get_expected_result() {
        InitScopeBasedIoCCommand().invoke()
        IoC.resolve(
            "IoC.Register",
            listOf("always_one", ILambda { _, _ -> 1 })
        )

        assertEquals(1, IoC.resolve("always_one"))
    }

    @Test
    fun WHEN_try_get_registered_command_from_different_scopes_EXPECT_get_expected_results() {
        InitScopeBasedIoCCommand().invoke()

        val baseScope = IoC.resolve("Scopes.Current")
        IoC.resolve(
            "IoC.Register",
            listOf("Scopes.Id", ILambda { _, _ -> 0 })
        )

        val scope1 = IoC.resolve("Scopes.New", baseScope)
        IoC.resolve("Scopes.Current.Set", scope1)
        IoC.resolve(
            "IoC.Register",
            listOf("Scopes.Id", ILambda { _, _ -> 1 })
        )

        val scope2 = IoC.resolve("Scopes.New", baseScope)
        IoC.resolve("Scopes.Current.Set", scope2)
        IoC.resolve(
            "IoC.Register",
            listOf("Scopes.Id", ILambda { _, _ -> 2 })
        )

        IoC.resolve("Scopes.Current.Set", baseScope)
        assertEquals(0, IoC.resolve("Scopes.Id"))

        IoC.resolve("Scopes.Current.Set", scope1)
        assertEquals(1, IoC.resolve("Scopes.Id"))

        IoC.resolve("Scopes.Current.Set", scope2)
        assertEquals(2, IoC.resolve("Scopes.Id"))
    }
    // endregion

    // region multi thread test
    @Test
    fun WHEN_try_set_new_scope_from_different_threads_EXPECT_get_expected_scopes() = runBlocking {
        InitScopeBasedIoCCommand().invoke()

        val baseScope = IoC.resolve("Scopes.Current")

        val jobs = List(5) {
            launch(Dispatchers.Default) {
                val scope = IoC.resolve("Scopes.New", baseScope)
                IoC.resolve("Scopes.Current.Set", scope)
                assertEquals(scope, IoC.resolve("Scopes.Current"))
            }
        }

        jobs.forEach { it.join() }
    }
    // endregion
}
