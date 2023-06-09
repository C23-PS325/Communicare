package c23.ps325.communicare.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import c23.ps325.communicare.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Fix the screen orientation for this sample to focus on cameraX API
        // rather than UI
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}