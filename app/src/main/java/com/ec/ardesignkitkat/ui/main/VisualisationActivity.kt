package com.ec.ardesignkitkat.ui.main

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ec.ardesignkitkat.R

class VisualisationActivity : AppCompatActivity() {

    private var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.visualisation)
        initialize()

    }

    fun initialize(){

    }

}