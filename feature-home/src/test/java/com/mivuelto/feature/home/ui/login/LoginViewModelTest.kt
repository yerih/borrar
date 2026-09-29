package com.mivuelto.feature.home.ui.login

import app.cash.turbine.test
import com.mivuelto.core.SerialNumberHolder
import com.mivuelto.core.domain.error.ApiError
import com.mivuelto.core.domain.model.AuthSession
import com.mivuelto.core.domain.model.AuthUser
import com.mivuelto.core.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val authRepository: AuthRepository = mockk(relaxed = true)
    private lateinit var viewModel: LoginViewModel

    private val authSession = AuthSession(
        sessionToken = "token",
        expiresAt = "2030-01-01T00:00:00",
        user = AuthUser(
            id = "u1",
            username = "admin",
            roleId = "ADM",
            merchantId = "m1"
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { authRepository.login(any(), any(), any()) } returns Result.success(authSession)
        viewModel = LoginViewModel(serialNumberHolder, authRepository)
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
    fun `OnLoginClicked when login fails sets error in state and does not navigate`() = runTest {
        coEvery { authRepository.login(any(), any(), any()) } returns
            Result.failure(ApiError.Unauthorized("Invalid credentials"))

        viewModel.onIntent(LoginIntent.OnUsernameChanged("admin"))
        viewModel.onIntent(LoginIntent.OnPasswordChanged("pass123"))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(LoginIntent.OnLoginClicked)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Invalid credentials", viewModel.state.value.error)
    }

    @Test
    fun `OnDismissError clears the error`() = runTest {
        coEvery { authRepository.login(any(), any(), any()) } returns
            Result.failure(ApiError.Unauthorized("Invalid credentials"))

        viewModel.onIntent(LoginIntent.OnUsernameChanged("admin"))
        viewModel.onIntent(LoginIntent.OnPasswordChanged("pass123"))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(LoginIntent.OnLoginClicked)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("Invalid credentials", viewModel.state.value.error)

        viewModel.onIntent(LoginIntent.OnDismissError)
        assertEquals(null, viewModel.state.value.error)
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
