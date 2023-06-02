package c23.ps325.communicare.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import c23.ps325.communicare.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}