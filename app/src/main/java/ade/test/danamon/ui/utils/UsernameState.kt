package ade.test.danamon.ui.utils

import java.util.regex.Pattern

private const val USERNAME_VALIDATION_REGEX = "^\\S+\$"

class UsernameState(val username: String? = null) :
    TextFieldState(validator = ::isUsernameValid, errorFor = ::usernameValidationError) {
    init {
        username?.let {
            text = it
        }
    }
}

/**
 * Returns an error to be displayed or null if no error was found
 */
private fun usernameValidationError(email: String): String {
    return "Invalid username: $email"
}

private fun isUsernameValid(username: String): Boolean {
    return Pattern.matches(USERNAME_VALIDATION_REGEX, username)
}

val UsernameStateSaver = textFieldStateSaver(UsernameState())
