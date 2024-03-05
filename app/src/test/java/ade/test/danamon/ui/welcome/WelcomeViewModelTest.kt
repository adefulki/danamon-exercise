package ade.test.danamon.ui.welcome

import ade.test.danamon.MainDispatcherRule
import ade.test.danamon.data.Result
import ade.test.danamon.domain.usecase.FindEmailUseCase
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WelcomeViewModelTest {

    private lateinit var vm: WelcomeViewModel

    lateinit var findEmailUseCase: FindEmailUseCase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        findEmailUseCase = mockk(relaxUnitFun = true)
        vm = WelcomeViewModel(
            findEmailUseCase
        )
    }

    @Test
    fun `isEmailExist, success response, data valid`() = runTest {
        //Given
        val email = "email"
        val userResult = Result.Success(true)
        coEvery {
            findEmailUseCase.invoke(any())
        } returns userResult

        //When
        vm.isEmailExist(email)
    }

}