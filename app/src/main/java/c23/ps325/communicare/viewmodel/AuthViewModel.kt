package c23.ps325.communicare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import c23.ps325.communicare.repository.AuthRepository
import c23.ps325.communicare.repository.errorResult
import c23.ps325.communicare.repository.getError
import c23.ps325.communicare.repository.isSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository): ViewModel(){

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean> = _navigate

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun login(username: String, password: String) {
        val loginLiveData = repository.login(username, password)

        loginLiveData.observeForever { result ->
            if (result.isSuccess()) {
                _navigate.value = true
                _errorMessage.value = "Login success"
            } else {
                _errorMessage.value = "Login failed. Please check your credentials."
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            val registerResult = repository.register(username, email, password)

            if (registerResult.isSuccess()) {
                _navigate.value = true
                _errorMessage.value = "Register Success"
            } else {
                _errorMessage.value = registerResult.getError()
            }
        }
    }

    fun patchUser(userData: String, username: String, password: String, email: String, photo : String) = repository.patchEditUser(userData, username, email, password, photo)
    fun patchObserver() = repository.dataEditObserver()

    fun getUser(username: String) = repository.getUser(username)
    fun userObserver() = repository.userObserver()

}