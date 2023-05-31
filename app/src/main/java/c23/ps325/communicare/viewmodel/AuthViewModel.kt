package c23.ps325.communicare.viewmodel

import androidx.lifecycle.ViewModel
import c23.ps325.communicare.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository): ViewModel(){

}