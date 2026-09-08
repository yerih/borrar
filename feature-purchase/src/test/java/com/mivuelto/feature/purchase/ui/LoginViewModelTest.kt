package com.mivuelto.feature.purchase.ui

import app.cash.turbine.test
import com.mivuelto.core.SerialNumberHolder
import com.mivuelto.feature.purchase.ui.login.LoginEffect
import com.mivuelto.feature.purchase.ui.login.LoginIntent
import com.mivuelto.feature.purchase.ui.login.LoginViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val serialNumberHolder: SerialNumberHolder = mockk(relaxed = true)
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(serialNumberHolder)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has empty user and password`() = runTest {
        viewModel.state.test {
            val state = awaitItem()
            assertEquals("", state.user)
            assertEquals("", state.password)
            assertEquals(false, state.isLoading)
            assertEquals(null, state.error)
            assertEquals(false, state.isLoginSuccess)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnUsernameChanged updates user in state`() = runTest {
        viewModel.onIntent(LoginIntent.OnUsernameChanged("admin"))
        viewModel.state.test {
            val state = awaitItem()
            assertEquals("admin", state.user)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnPasswordChanged updates password in state`() = runTest {
        viewModel.onIntent(LoginIntent.OnPasswordChanged("pass123"))
        viewModel.state.test {
            val state = awaitItem()
            assertEquals("pass123", state.password)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnLoginClicked with valid credentials sends NavigateToHome effect`() = runTest {
        viewModel.onIntent(LoginIntent.OnUsernameChanged("admin"))
        viewModel.onIntent(LoginIntent.OnPasswordChanged("pass123"))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(LoginIntent.OnLoginClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effect.test {
            val effect = awaitItem()
            assertEquals(true, effect is LoginEffect.NavigateToHome)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnLoginClicked with empty user sends TextFieldErrors effect`() = runTest {
        viewModel.onIntent(LoginIntent.OnUsernameChanged(""))
        viewModel.onIntent(LoginIntent.OnPasswordChanged("pass123"))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(LoginIntent.OnLoginClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effect.test {
            val effect = awaitItem()
            assertEquals(true, effect is LoginEffect.TextFieldErrors)
            val errors = effect as LoginEffect.TextFieldErrors
            assertEquals(true, errors.userError)
            assertEquals(false, errors.passwordError)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnLoginClicked with empty password sends TextFieldErrors effect`() = runTest {
        viewModel.onIntent(LoginIntent.OnUsernameChanged("admin"))
        viewModel.onIntent(LoginIntent.OnPasswordChanged(""))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(LoginIntent.OnLoginClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effect.test {
            val effect = awaitItem()
            assertEquals(true, effect is LoginEffect.TextFieldErrors)
            val errors = effect as LoginEffect.TextFieldErrors
            assertEquals(false, errors.userError)
            assertEquals(true, errors.passwordError)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnLoginClicked with both empty sends TextFieldErrors with both errors`() = runTest {
        viewModel.onIntent(LoginIntent.OnUsernameChanged(""))
        viewModel.onIntent(LoginIntent.OnPasswordChanged(""))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(LoginIntent.OnLoginClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effect.test {
            val effect = awaitItem()
            assertEquals(true, effect is LoginEffect.TextFieldErrors)
            val errors = effect as LoginEffect.TextFieldErrors
            assertEquals(true, errors.userError)
            assertEquals(true, errors.passwordError)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `checkCredentials returns true for valid input`() = runTest {
        viewModel.onIntent(LoginIntent.OnUsernameChanged("admin"))
        viewModel.onIntent(LoginIntent.OnPasswordChanged("pass123"))
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.checkCredentials()
        assertEquals(true, result)
    }

    @Test
    fun `checkCredentials returns false for empty user`() = runTest {
        viewModel.onIntent(LoginIntent.OnUsernameChanged(""))
        viewModel.onIntent(LoginIntent.OnPasswordChanged("pass123"))
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.checkCredentials()
        assertEquals(false, result)
    }

    @Test
    fun `checkCredentials returns false for empty password`() = runTest {
        viewModel.onIntent(LoginIntent.OnUsernameChanged("admin"))
        viewModel.onIntent(LoginIntent.OnPasswordChanged(""))
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.checkCredentials()
        assertEquals(false, result)
    }
}
