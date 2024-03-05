package ade.test.danamon.ui.home

import ade.test.danamon.MainDispatcherRule
import ade.test.danamon.TestDataClassGenerator
import ade.test.danamon.data.Result
import ade.test.danamon.data.datasource.PhotoRemoteDataSource
import ade.test.danamon.domain.model.User
import ade.test.danamon.domain.usecase.DeleteUserUseCase
import ade.test.danamon.domain.usecase.EditUserUseCase
import ade.test.danamon.domain.usecase.GetPhotosUseCase
import ade.test.danamon.domain.usecase.GetUsersUseCase
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    private lateinit var vm: HomeViewModel

    lateinit var getPhotosUseCase: GetPhotosUseCase
    lateinit var getUsersUseCase: GetUsersUseCase
    lateinit var deleteUserUseCase: DeleteUserUseCase
    lateinit var editUserUseCase: EditUserUseCase
    lateinit var photoRemoteDataSource: PhotoRemoteDataSource

    private val testDataClassGenerator: TestDataClassGenerator = TestDataClassGenerator()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        getPhotosUseCase = mockk(relaxUnitFun = true)
        getUsersUseCase = mockk(relaxUnitFun = true)
        deleteUserUseCase = mockk(relaxUnitFun = true)
        editUserUseCase = mockk(relaxUnitFun = true)
        vm = HomeViewModel(
            getPhotosUseCase,
            getUsersUseCase,
            deleteUserUseCase,
            editUserUseCase
        )
    }

    @Test
    fun `getUsers, success response, data valid`() = runTest {
        //Given
        val users = listOf(User("id", "email", "username", "password", "Admin", "token"))
        val usersResult = Result.Success(users)
        coEvery {
            getUsersUseCase()
        } returns usersResult

        //When
        vm.getUsers()

        //Then
        assertEquals(vm.usersState.value, users)
    }

    @Test
    fun `getUsers, failed response, data valid`() = runTest {
        //Given
        val usersResult = Result.Error<List<User>>("error", null)
        coEvery {
            getUsersUseCase()
        } returns usersResult

        //When
        vm.getUsers()

        //Then
        assertEquals(vm.usersState.value, emptyList<User>())
    }

    @Test
    fun `getUsers, loading response, data valid`() = runTest {
        //Given
        val usersResult = Result.Loading<List<User>>(null)
        coEvery {
            getUsersUseCase()
        } returns usersResult

        //When
        vm.getUsers()

        //Then
        assertEquals(vm.usersState.value, emptyList<User>())
    }

    @Test
    fun `deleteUser, success response, data valid`() = runTest {
        //Given
        val deleteUsersResult = Result.Success(true)
        coEvery {
            deleteUserUseCase.invoke(any())
        } returns deleteUsersResult
        val users = listOf(User("id", "email", "username", "password", "Admin", "token"))
        val usersResult = Result.Success(users)
        coEvery {
            getUsersUseCase()
        } returns usersResult

        //When
        vm.deleteUser(User("id", "email", "username", "password", "Admin", "token"))

        //Then
        coVerify { vm.getUsers() }
        assertEquals(vm.usersState.value, users)
    }

    @Test
    fun `deleteUser, failed response, data valid`() = runTest {
        //Given
        val deleteUsersResult = Result.Error("error", false)
        coEvery {
            deleteUserUseCase.invoke(any())
        } returns deleteUsersResult
        val users = listOf(User("id", "email", "username", "password", "Admin", "token"))
        val usersResult = Result.Success(users)
        coEvery {
            getUsersUseCase()
        } returns usersResult

        //When
        vm.deleteUser(User("id", "email", "username", "password", "Admin", "token"))

        //Then
        coVerify { vm.getUsers() }
        assertEquals(vm.usersState.value, users)
    }

    @Test
    fun `deleteUser, loading response, data valid`() = runTest {
        //Given
        val deleteUsersResult = Result.Loading(false)
        coEvery {
            deleteUserUseCase.invoke(any())
        } returns deleteUsersResult
        val users = listOf(User("id", "email", "username", "password", "Admin", "token"))
        val usersResult = Result.Success(users)
        coEvery {
            getUsersUseCase()
        } returns usersResult

        //When
        vm.deleteUser(User("id", "email", "username", "password", "Admin", "token"))

        //Then
        coVerify { vm.getUsers() }
        assertEquals(vm.usersState.value, users)
    }

    @Test
    fun `editUser, success response, data valid`() = runTest {
        //Given
        val editUsersResult = Result.Success(true)
        coEvery {
            editUserUseCase.invoke(any())
        } returns editUsersResult
        val users = listOf(User("id", "email", "username", "password", "Admin", "token"))
        val usersResult = Result.Success(users)
        coEvery {
            getUsersUseCase()
        } returns usersResult

        //When
        vm.editUser(ade.test.danamon.data.local.model.User())

        //Then
        coVerify { vm.getUsers() }
        assertEquals(vm.usersState.value, users)
    }

    @Test
    fun `editUser, failed response, data valid`() = runTest {
        //Given
        val editUsersResult = Result.Error("error", false)
        coEvery {
            editUserUseCase.invoke(any())
        } returns editUsersResult

        //When
        vm.editUser(ade.test.danamon.data.local.model.User())

        //Then
        assertEquals(vm.usersState.value, emptyList<User>())
    }

    @Test
    fun `editUser, loading response, data valid`() = runTest {
        //Given
        val editUsersResult = Result.Loading(false)
        coEvery {
            editUserUseCase.invoke(any())
        } returns editUsersResult

        //When
        vm.editUser(ade.test.danamon.data.local.model.User())

        //Then
        assertEquals(vm.usersState.value, emptyList<User>())
    }

    @Test
    fun `onItemCollapsed, success response, data valid`() = runTest {
        //Given
        val id = "id"

        //When
        vm.onItemExpanded(id)
        vm.onItemCollapsed(id)

        //Then
        assertEquals(vm.revealedCardIdsList.value.contains(id), false)
    }

    @Test
    fun `onItemExpanded, success response, data valid`() = runTest {
        //Given
        val id = "id"

        //When
        vm.onItemExpanded(id)

        //Then
        assertEquals(vm.revealedCardIdsList.value.contains(id), true)
    }
}