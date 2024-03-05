package ade.test.danamon.domain.usecase

import ade.test.danamon.data.Result
import ade.test.danamon.domain.repo.UserRepo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class FindEmailUseCase @Inject constructor(
    private val repository: UserRepo
) {

    suspend operator fun invoke(email: String): Result<Boolean> {
        val data = repository.findEmail(email).first()
        if (data.obj != null) {
            return Result.Success(true)
        }
        return Result.Error("Email Not Found", false)
    }
}