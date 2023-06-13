package c23.ps325.communicare.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.concurrent.futures.await
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import c23.ps325.communicare.databinding.FragmentCameraBinding
import c23.ps325.communicare.model.DataItem
import c23.ps325.communicare.model.TextScript
import c23.ps325.communicare.ui.adapter.ScriptAdapter
import c23.ps325.communicare.viewmodel.ScriptViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
@AndroidEntryPoint
class CameraFragment : Fragment(){

    private var _binding : FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val model : ScriptViewModel by viewModels()
    private val adapter by lazy { ScriptAdapter() }

    private var lensFacing: Int = CameraSelector.LENS_FACING_FRONT
    private var preview: Preview? = null
    private lateinit var videoCapture: VideoCapture<Recorder>
    private lateinit var viewFinder: PreviewView
    private var currentRecording: Recording? = null
    private lateinit var recordingState:VideoRecordEvent
    private var audioEnabled = true
    private var cameraIndex = 0
    private var qualityIndex = DEFAULT_QUALITY_IDX

    /** Host's navigation controller */
    private val navController: NavController by lazy {
        Navigation.findNavController(requireActivity(), c23.ps325.communicare.R.id.nav_host_fragment_container)
    }

    private val mainThreadExecutor by lazy { ContextCompat.getMainExecutor(requireContext()) }
    private var enumerationDeferred: Deferred<Unit>? = null

    // Camera UI  states and inputs
    enum class UiState {
        IDLE,       // Not recording, all UI controls are active.
        RECORDING,  // Camera is recording, only display Pause/Resume & Stop button.
        FINALIZED,  // Recording just completes, disable all RECORDING UI controls.
    }

    //handle scroll count
    var scrollCount: Int = 0

    private lateinit var layout: LinearLayoutManager

    //handler for run auto scroll thread
    internal val handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCameraFragment()

        viewFinder = binding.previewView
    }

    private fun initCameraFragment() {
        initializeUI()
        viewLifecycleOwner.lifecycleScope.launch {
            if (enumerationDeferred != null) {
                enumerationDeferred!!.await()
                enumerationDeferred = null
            }
            bindCameraUseCases()
        }
    }

    private fun initializeScriptUI() {
        binding.listScript.adapter = adapter
        layout = object : LinearLayoutManager(requireContext(), VERTICAL, false){
            override fun smoothScrollToPosition(
                recyclerView: RecyclerView?,
                state: RecyclerView.State?,
                position: Int
            ) {
                val smoothScroller = object : LinearSmoothScroller(requireContext()){
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                        return 5.0f
                    }
                }
                smoothScroller.targetPosition = position
                startSmoothScroll(smoothScroller)
            }
        }
        binding.listScript.layoutManager = layout
        autoScroll()
    }

    private fun autoScroll() {
        scrollCount = 0
        val speedScroll: Long = 3000
        val runnable = object : Runnable {
            override fun run() {
                if (layout.findFirstVisibleItemPosition() >= adapter.itemCount / 2) {
                    binding.listScript.adapter = adapter
                    Log.e(TAG, "run: load $scrollCount")
                }
                binding.listScript.smoothScrollToPosition(scrollCount++)
                Log.e(TAG, "run: $scrollCount")
                handler.postDelayed(this, speedScroll)
            }
        }
        handler.postDelayed(runnable, speedScroll)
    }

    private fun setDataScript(){
        model.script(1)
        model.scriptObserver().observe(viewLifecycleOwner){
            if (it != null){
                adapter.setData(it)
                initializeScriptUI()
                Log.i(TAG, "setDataScript: Success")
            }else{
                Toast.makeText(context, "Script Null", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initializeUI() {

        binding.switchCamera.setOnClickListener {
            lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                CameraSelector.LENS_FACING_BACK
            } else {
                CameraSelector.LENS_FACING_FRONT
            }
            // Re-bind use cases to update selected camera
            viewLifecycleOwner.lifecycleScope.launch { bindCameraUseCases() }
        }

        // React to user touching the capture button
        binding.captureButton.apply {
            setOnClickListener {
                if (!this@CameraFragment::recordingState.isInitialized ||
                    recordingState is VideoRecordEvent.Finalize)
                {
                    enableUI(false)  // Our eventListener will turn on the Recording UI.
                    startVideoCapture()
                } else {
                    when (recordingState) {
                        is VideoRecordEvent.Start -> {
                            currentRecording?.pause()
                            binding.stopButton.visibility = View.VISIBLE
                        }
                        /*is VideoRecordEvent.Pause -> currentRecording?.resume()
                        is VideoRecordEvent.Resume -> currentRecording?.pause()*/
                        else -> throw IllegalStateException("recordingState in unknown state")
                    }
                }
            }
            isEnabled = false
            setDataScript()
        }

       binding.stopButton.apply {
            setOnClickListener {
                // stopping: hide it after getting a click before we go to viewing fragment
                binding.stopButton.visibility = View.INVISIBLE
                if (currentRecording == null || recordingState is VideoRecordEvent.Finalize) {
                    return@setOnClickListener
                }

                val recording = currentRecording
                if (recording != null) {
                    recording.stop()
                    currentRecording = null
                }
                binding.captureButton.setImageResource(c23.ps325.communicare.R.drawable.ic_start)
            }
            // ensure the stop button is initialized disabled & invisible
            visibility = View.INVISIBLE
            isEnabled = false
        }

    }

    private suspend fun bindCameraUseCases() {
        val qualitySelector = QualitySelector.from(Quality.SD)
        val cameraProvider = ProcessCameraProvider.getInstance(requireContext()).await()

        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        preview = Preview.Builder()
            .build().apply {
                setSurfaceProvider(viewFinder.surfaceProvider)
            }

        // build a recorder, which can:
        //   - record video/audio to MediaStore(only shown here), File, ParcelFileDescriptor
        //   - be used create recording(s) (the recording performs recording)
        val recorder = Recorder.Builder()
            .setQualitySelector(qualitySelector)
            .build()
        videoCapture = VideoCapture.withOutput(recorder)

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector,
                videoCapture,
                preview
            )
        } catch (exc: Exception) {
            // we are on main thread, let's reset the controls on the UI.
            Log.e(TAG, "Use case binding failed", exc)
            resetUIandState("bindToLifecycle failed: $exc")
        }
        enableUI(true)
    }

    private fun enableUI(enable: Boolean) {
        arrayOf(binding.switchCamera,
            binding.captureButton,
            binding.stopButton).forEach {
            it.isEnabled = enable
        }
    }

    private fun showUI(state: UiState, status:String = "idle") {
        binding.let {
            when(state) {
                UiState.IDLE -> {
                    it.captureButton.setImageResource(c23.ps325.communicare.R.drawable.ic_start)
                    it.stopButton.visibility = View.INVISIBLE

                    it.switchCamera.visibility= View.VISIBLE
                }
                UiState.RECORDING -> {
                    it.switchCamera.visibility = View.INVISIBLE
                    it.captureButton.isEnabled = true
                    it.stopButton.visibility = View.VISIBLE
                    it.stopButton.isEnabled = true
                }
                UiState.FINALIZED -> {
                    it.captureButton.setImageResource(c23.ps325.communicare.R.drawable.ic_start)
                    it.stopButton.visibility = View.INVISIBLE
                }
                else -> {
                    val errorMsg = "Error: showUI($state) is not supported"
                    Log.e(TAG, errorMsg)
                    return
                }
            }
        }
    }

    private fun updateUI(event: VideoRecordEvent) {
        val state = if (event is VideoRecordEvent.Status) recordingState.recordingStats
        else event.recordingStats
        when (event) {
            is VideoRecordEvent.Status -> {
                // placeholder: we update the UI with new status after this when() block,
                // nothing needs to do here.
            }
            is VideoRecordEvent.Start -> {
                showUI(UiState.RECORDING, event.recordingStats.toString())
            }
            is VideoRecordEvent.Finalize-> {
                showUI(UiState.FINALIZED, event.recordingStats.toString())
            }
        }

        val stats = event.recordingStats
        val size = stats.numBytesRecorded / 1000
        val time = java.util.concurrent.TimeUnit.NANOSECONDS.toSeconds(stats.recordedDurationNanos)
        var text = "${state}: recorded ${size}KB, in ${time}second"
        if(event is VideoRecordEvent.Finalize)
            text = "${text}\nFile saved to: ${event.outputResults.outputUri}"
        Log.i(TAG, "updateUI: $text")

    }

    @SuppressLint("MissingPermission")
    fun startVideoCapture() {
        // create MediaStoreOutputOptions for our recorder: resulting our recording!
        val name = "Communicare-recording-" +
                SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                    .format(System.currentTimeMillis()) + ".mp4"
        val contentValues = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, name)
        }
        val mediaStoreOutput = MediaStoreOutputOptions.Builder(
            requireActivity().contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        // configure Recorder and Start recording to the mediaStoreOutput.
        currentRecording = videoCapture.output
            .prepareRecording(requireActivity(), mediaStoreOutput)
            .apply { if (audioEnabled) withAudioEnabled() }
            .start(mainThreadExecutor, captureListener)

        Log.i(TAG, "Recording started")
    }

    /**
     * CaptureEvent listener.
     */
    private val captureListener = Consumer<VideoRecordEvent> { event ->
        // cache the recording state
        if (event !is VideoRecordEvent.Status)
            recordingState = event

        updateUI(event)

        if (event is VideoRecordEvent.Finalize) {
            // display the captured video
            lifecycleScope.launch {
                navController.navigate(
                    CameraFragmentDirections.actionCameraFragmentToUploadFragment(event.outputResults.outputUri)
                )
            }
        }
    }

    private fun resetUIandState(reason: String) {
        enableUI(true)
        showUI(UiState.IDLE, reason)

        cameraIndex = 0
        qualityIndex = DEFAULT_QUALITY_IDX
        audioEnabled = true
        initializeScriptUI()
    }

    companion object {
        @JvmStatic
        fun newInstance() = CameraFragment()
        const val DEFAULT_QUALITY_IDX = 0
        val TAG:String = CameraFragment::class.java.simpleName
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

}