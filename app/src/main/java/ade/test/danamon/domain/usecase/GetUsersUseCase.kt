package ade.test.danamon.domain.usecase

import ade.test.danamon.data.Result
import ade.test.danamon.data.local.model.toModelUser
import ade.test.danamon.domain.model.User
import ade.test.danamon.domain.repo.UserRepo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepo
) {
    suspend operator fun invoke(): Result<List<User>> {
        val data = repository.getUsers().first()
        return Result.Success(data.list.toList().map { it.toModelUser() })
    }
}