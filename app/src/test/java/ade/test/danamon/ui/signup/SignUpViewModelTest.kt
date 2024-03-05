package ade.test.danamon.ui.signup

import ade.test.danamon.MainDispatcherRule
import ade.test.danamon.data.Result
import ade.test.danamon.domain.model.User
import ade.test.danamon.domain.usecase.RegisterUseCase
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignUpViewModelTest {

    private lateinit var vm: SignUpViewModel

    lateinit var registerUseCase: RegisterUseCase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        registerUseCase = mockk(relaxUnitFun = true)
        vm = SignUpViewModel(
            registerUseCase
        )
    }

    @Test
    fun `register, success response, data valid`() = runTest {
        //Given
        val email = "email"
        val username = "username"
        val role = Role.User
        val password = "password"
        val user = User("id", email, "username", password, "Admin", "token")
        val userResult = Result.Success(true)
        coEvery {
            registerUseCase.invoke(any())
        } returns userResult

        //When
        vm.register(email, username, password, role)
    }
}