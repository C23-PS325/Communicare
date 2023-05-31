package c23.ps325.communicare.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import c23.ps325.communicare.repository.PrefRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(private val repository: PrefRepository): ViewModel(){

    fun liveThemeSettings() : LiveData<Boolean> {
        return repository.getThemeSetting().asLiveData()
    }

    fun getStatus() = repository.getStatus().asLiveData()

    fun getName() = repository.getName().asLiveData()

    fun saveThemeSettings(isDarkModeActive: Boolean){
        viewModelScope.launch {
            repository.saveTheme(isDarkModeActive)
        }
    }

    fun saveLogin(status: Boolean, name: String){
        viewModelScope.launch {
            repository.saveLogin(status, name)
        }
    }

    fun logout(){
        viewModelScope.launch { repository.delete() }
    }
}