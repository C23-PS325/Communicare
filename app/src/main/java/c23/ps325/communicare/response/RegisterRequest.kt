package c23.ps325.communicare.response

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)
