package ade.test.danamon.domain.usecase

import ade.test.danamon.data.Result
import ade.test.danamon.data.local.model.toModelUser
import ade.test.danamon.domain.model.User
import ade.test.danamon.domain.repo.UserRepo
import ade.test.danamon.utils.JwsUtils
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: UserRepo
) {

    suspend operator fun invoke(email: String, password: String): Result<User> {
        val data = repository.findUser(email, password).first()
        data.obj?.let {
            val user = it.toModelUser()
            user.token = JwsUtils.generateJws()
            return Result.Success(user)
        }
        return Result.Error("Sign in failed", null)
    }
}