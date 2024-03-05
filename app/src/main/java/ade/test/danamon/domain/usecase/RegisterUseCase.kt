package ade.test.danamon.domain.usecase

import ade.test.danamon.data.Result
import ade.test.danamon.data.local.model.User
import ade.test.danamon.domain.repo.UserRepo
import io.realm.kotlin.ext.isValid
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: UserRepo
) {

    suspend operator fun invoke(user: User): Result<Boolean> {
        val userRegistered = repository.findEmail(user.email)
        if (userRegistered.first().obj != null) {
            return Result.Error("User already register", false)
        }
        val data = repository.insertUser(user)
        if (data.isValid()) {
            return Result.Success(true)
        }
        return Result.Error("User register failed", false)
    }
}