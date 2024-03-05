package ade.test.danamon.domain.repo

import ade.test.danamon.data.local.model.User
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.SingleQueryChange
import kotlinx.coroutines.flow.Flow

interface UserRepo {

    suspend fun insertUser(user: User): User

    fun findUser(email: String, password: String): Flow<SingleQueryChange<User>>

    fun findEmail(email: String): Flow<SingleQueryChange<User>>

    fun getUsers(): Flow<ResultsChange<User>>

    suspend fun updateUser(user: User): User?

    suspend fun deleteUser(id: String)
}