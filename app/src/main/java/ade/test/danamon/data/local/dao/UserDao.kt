package ade.test.danamon.data.local.dao

import ade.test.danamon.data.local.model.User
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.SingleQueryChange
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDao @Inject constructor(
    private val realm: Realm
) {
    suspend fun insertUser(user: User) = realm.write {
        copyToRealm(user, updatePolicy = UpdatePolicy.ALL)
    }

    // fetch all objects of a type as a flow, asynchronously
    fun getAllUsers(): Flow<ResultsChange<User>> = realm.query<User>().asFlow()

    fun findUser(email: String, password: String): Flow<SingleQueryChange<User>> =
        realm.query<User>("email == $0 AND password == $1", email, password).first().asFlow()

    fun findEmail(email: String): Flow<SingleQueryChange<User>> =
        realm.query<User>("email == $0", email).first().asFlow()

    suspend fun deleteUser(id: String) = realm.write {

        val findUser = query<User>("id == $0", id).find()
        delete(findUser)
    }

    suspend fun updateUser(users: User?) = realm.write {

        val findUser = query<User>("id == $0", users?.id ?: "0").first().find()

        findUser?.apply {
            email = users?.email ?: findUser.email
            password = users?.password ?: findUser.password
            username = users?.username ?: findUser.username
            role = users?.role ?: findUser.role
        }
    }

}