package c23.ps325.communicare.viewmodel

import androidx.lifecycle.ViewModel
import c23.ps325.communicare.repository.VideoPredictRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class VideoPredictViewModel @Inject constructor(private val repository: VideoPredictRepository) : ViewModel() {

    fun uploadVideo(video : MultipartBody.Part) = repository.uploadVideo(video)

    fun uploadObserver() = repository.doUploadObserver()

    fun loadingObserver() = repository.isLoading()
}