package ade.test.danamon.ui.signin

import ade.test.danamon.MainDispatcherRule
import ade.test.danamon.data.Result
import ade.test.danamon.domain.model.User
import ade.test.danamon.domain.usecase.LoginUseCase
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInViewModelTest {

    private lateinit var vm: SignInViewModel

    lateinit var loginUseCase: LoginUseCase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        loginUseCase = mockk(relaxUnitFun = true)
        vm = SignInViewModel(
            loginUseCase
        )
    }

    @Test
    fun `login, success response, data valid`() = runTest {
        //Given
        val email = "email"
        val password = "password"
        val user = User("id", email, "username", password, "Admin", "token")
        val userResult = Result.Success(user)
        coEvery {
            loginUseCase.invoke(any(), any())
        } returns userResult

        //When
        vm.login(email, password)
    }
}