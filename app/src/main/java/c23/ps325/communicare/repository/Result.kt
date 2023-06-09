package c23.ps325.communicare.repository

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String?) : Result<Nothing>()
}

fun <T> errorResult(message: String?): Result<T> {
    return Result.Error(message)
}

fun <T> T.toSuccessResult(): Result<T> {
    return Result.Success(this)
}

fun <T> Result<T>.isSuccess(): Boolean {
    return this is Result.Success
}

fun <T> Result<T>.getError(): String? {
    return if (this is Result.Error) {
        message
    } else {
        null
    }
}



