package ade.test.danamon.domain.model


data class User(
    var id: String,
    var email: String,
    var username: String,
    var password: String,
    var role: String,
    var token: String
)

fun User.toModelUser(): ade.test.danamon.data.local.model.User {
    val user = ade.test.danamon.data.local.model.User()
    user.let {
        it.id = this.id
        it.email = this.email
        it.username = this.username
        it.role = this.role
        it.password = password
    }
    return user
}