package ade.test.danamon.domain.usecase

import ade.test.danamon.data.Result
import ade.test.danamon.data.local.model.User
import ade.test.danamon.domain.repo.UserRepo
import io.realm.kotlin.ext.isValid
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val repository: UserRepo
) {

    suspend operator fun invoke(id: String): Result<Boolean> {
        repository.deleteUser(id)
        return Result.Success(true)
    }
}