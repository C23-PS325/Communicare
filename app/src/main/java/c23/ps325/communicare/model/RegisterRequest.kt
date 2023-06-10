package c23.ps325.communicare.model

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)
