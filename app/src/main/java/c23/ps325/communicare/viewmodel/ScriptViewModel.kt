package c23.ps325.communicare.viewmodel

import androidx.lifecycle.ViewModel
import c23.ps325.communicare.repository.ScriptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScriptViewModel @Inject constructor(private val repo : ScriptRepository): ViewModel() {
    fun script(id : Int) = repo.getScript(id)

    fun scriptObserver() = repo.scriptObserver()
}