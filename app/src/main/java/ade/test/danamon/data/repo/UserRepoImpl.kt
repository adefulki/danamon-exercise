package ade.test.danamon.data.repo

import ade.test.danamon.data.local.dao.UserDao
import ade.test.danamon.data.local.model.User
import ade.test.danamon.domain.repo.UserRepo
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.SingleQueryChange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepo {

    override suspend fun insertUser(user: User) = withContext(Dispatchers.IO) {
        userDao.insertUser(user)
    }

    override fun findUser(email: String, password: String): Flow<SingleQueryChange<User>> {
        return userDao.findUser(email, password)
    }

    override fun findEmail(email: String): Flow<SingleQueryChange<User>> {
        return userDao.findEmail(email)
    }

    override fun getUsers(): Flow<ResultsChange<User>> {
        return userDao.getAllUsers()
    }

    override suspend fun updateUser(user: User) = withContext(Dispatchers.IO) {
        userDao.updateUser(user)
    }

    override suspend fun deleteUser(id: String) = withContext(Dispatchers.IO) {
        userDao.deleteUser(id)
    }
}