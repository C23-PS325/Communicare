package c23.ps325.communicare.ui

import android.database.Cursor
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import c23.ps325.communicare.R
import c23.ps325.communicare.database.PredictionHistory
import c23.ps325.communicare.databinding.FragmentUploadBinding
import c23.ps325.communicare.viewmodel.HistoryViewModel
import c23.ps325.communicare.viewmodel.VideoPredictViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*

@AndroidEntryPoint
class UploadFragment : Fragment() {

    private val args: UploadFragmentArgs by navArgs()

    private var _binding : FragmentUploadBinding? = null
    private val binding get() = _binding!!

    private val model : VideoPredictViewModel by viewModels()
    private val historyViewModel : HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            showVideo(args.uri)
        } else {
            // force MediaScanner to re-scan the media file.
            val path = getAbsolutePathFromUri(args.uri) ?: return
            MediaScannerConnection.scanFile(
                context, arrayOf(path), null
            ) { _, uri ->
                // playback video on main thread with VideoView
                if (uri != null) {
                    lifecycleScope.launch {
                        showVideo(uri)
                    }
                }
            }
        }
        Log.i("URI VIDEO", "onViewCreated: ${args.uri}")
        Log.i("URI VIDEO", "onViewCreated: ${getAbsolutePathFromUri(args.uri)}")

        model.loadingObserver().observe(viewLifecycleOwner){
            loading(it)
        }
        //Handle upload Button
        binding.btnUpload.setOnClickListener {
            uploadVideo()
        }

        // Handle back button press
        binding.btnBack.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_container).navigateUp()
        }
    }

    private fun uploadVideo() {
        // TODO: upload video to cloud
        val path = getAbsolutePathFromUri(args.uri)
        MediaScannerConnection.scanFile(
            context, arrayOf(path), null
        ) { _, uri ->
            val tipe = requireContext().contentResolver?.getType(uri)
            val tempFile = File.createTempFile("video", ".mp4",null)
            val inputStream = requireContext().contentResolver?.openInputStream(uri)
            tempFile.outputStream().use {
                inputStream?.copyTo(it)
            }
            val reqBody : RequestBody = tempFile.asRequestBody(tipe?.toMediaType())
            val image = MultipartBody.Part.createFormData("file_video", tempFile.name, reqBody)
            model.uploadVideo(image)
            Log.i("Upload Video", "Success, $image")
            inputStream?.close()

            lifecycleScope.launch {
                withContext(Dispatchers.Main){
                    model.uploadObserver().observe(viewLifecycleOwner){
                        if (it != null) {
                            val history = PredictionHistory(
                                0,
                                it.data.frames.angry,
                                it.data.frames.fear,
                                it.data.frames.happy,
                                it.data.frames.sad,
                                it.data.frames.surprise,
                                it.data.audio,
                                Date().toString()
                            )
                            lifecycleScope.launch {
                                historyViewModel.addHistory(history)
                            }
                            val bun = Bundle()
                            bun.putParcelable("result_predict", it)
                            Navigation.findNavController(requireView()).navigate(R.id.action_uploadFragment_to_resultFragment, bun)
                        }
                    }
                }
            }
        }

    }

    private fun getAbsolutePathFromUri(contentUri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            cursor = requireContext()
                .contentResolver
                .query(contentUri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
            if (cursor == null) {
                return null
            }
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)
        } catch (e: RuntimeException) {
            Log.e("VideoViewerFragment", String.format(
                "Failed in getting absolute path for Uri %s with Exception %s",
                contentUri.toString(), e.toString()
            )
            )
            null
        } finally {
            cursor?.close()
        }
    }

    private fun showVideo(uri: Uri) {
        val fileSize = getFileSizeFromUri(uri)
        if (fileSize == null || fileSize <= 0) {
            Log.e("VideoViewerFragment", "Failed to get recorded file size, could not be played!")
            return
        }

        val filePath = getAbsolutePathFromUri(uri) ?: return
        val fileInfo = "FileSize: $fileSize\n $filePath"
        Log.i("VideoViewerFragment", fileInfo)

        val mc = MediaController(requireContext())
        binding.videoView.apply {
            setVideoURI(uri)
            setMediaController(mc)
            requestFocus()
        }.start()
        mc.show(0)
    }

    private fun getFileSizeFromUri(contentUri: Uri): Long? {
        val cursor = requireContext()
            .contentResolver
            .query(contentUri, null, null, null, null)
            ?: return null

        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        cursor.moveToFirst()

        cursor.use {
            return it.getLong(sizeIndex)
        }
    }

    private fun loading(status: Boolean) {
        when(status){
            true -> {
                binding.loadingBar.visibility = View.VISIBLE

            }
            false -> {
                binding.loadingBar.visibility = View.GONE

            }
        }
    }
}