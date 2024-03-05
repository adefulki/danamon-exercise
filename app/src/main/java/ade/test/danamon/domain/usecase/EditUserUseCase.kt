package ade.test.danamon.domain.usecase

import ade.test.danamon.data.Result
import ade.test.danamon.data.local.model.User
import ade.test.danamon.domain.repo.UserRepo
import io.realm.kotlin.ext.isValid
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class EditUserUseCase @Inject constructor(
    private val repository: UserRepo
) {

    suspend operator fun invoke(user: User): Result<Boolean> {
        val data = repository.updateUser(user)
        if (data?.isValid() == true) {
            return Result.Success(true)
        }
        return Result.Error("User edit failed", false)
    }
}