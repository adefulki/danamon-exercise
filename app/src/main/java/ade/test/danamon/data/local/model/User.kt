package ade.test.danamon.data.local.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class User() : RealmObject {

    @PrimaryKey
    var id: String = ""
    var email: String = ""
    var username: String = ""
    var password: String = ""
    var role: String = ""

    constructor(
        id: String = UUID.randomUUID().toString(),
        email: String,
        username: String,
        password: String,
        role: String
    ) : this() {
        this.email = email
        this.username = username
        this.password = password
        this.id = id
        this.role = role
    }
}

fun User.toModelUser(): ade.test.danamon.domain.model.User {
    return ade.test.danamon.domain.model.User(
        id = this.id,
        email = this.email,
        username = this.username,
        role = this.role,
        password = password,
        token = ""
    )
}