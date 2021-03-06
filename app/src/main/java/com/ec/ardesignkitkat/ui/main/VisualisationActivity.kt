package com.ec.ardesignkitkat.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.ec.ardesignkitkat.CameraPreview
import com.ec.ardesignkitkat.R
import com.vikramezhil.droidspeech.DroidSpeech
import com.vikramezhil.droidspeech.OnDSListener
import com.vikramezhil.droidspeech.OnDSPermissionsListener
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class VisualisationActivity : AppCompatActivity(), View.OnClickListener, OnDSListener,
    OnDSPermissionsListener {

    private var startSpeech: Button? = null
    private var stopSpeech: Button? = null
    private var droidSpeech: DroidSpeech?= null

    private var btnPrendrePhoto: Button? = null

    private var camera:Camera? = null
    private var preview: CameraPreview? = null
    private var mediaRecorder: MediaRecorder? =null

    val MEDIA_TYPE_IMAGE = 1
    val MEDIA_TYPE_VIDEO = 2

    var TAGCamera = "MyCameraApp"

    var TAG = "DroidSpeech 3"

    private var internetEnabled = true;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.visualisation)
        initialize()
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)

    }

    override fun onPause() {
        super.onPause()
        releaseMediaRecorder() // if you are using MediaRecorder, release it first
        releaseCamera() // release the camera immediately on pause event
    }

    override fun onStop() {
        super.onStop()
        releaseMediaRecorder() // if you are using MediaRecorder, release it first
        releaseCamera() // release the camera immediately on pause event
    }

    private fun releaseMediaRecorder() {
        mediaRecorder?.reset() // clear recorder configuration
        mediaRecorder?.release() // release the recorder object
        mediaRecorder = null
        camera?.lock() // lock camera for later use
    }

    private fun releaseCamera() {
        camera?.release() // release the camera for other applications
        camera = null
        Log.i(TAGCamera, "Release camera")
    }

    fun initialize(){
        btnPrendrePhoto = findViewById(R.id.capture_button)
        btnPrendrePhoto?.setOnClickListener(this)

        droidSpeech = DroidSpeech(this, null)
        droidSpeech!!.setOnDroidSpeechListener(this)
        droidSpeech!!.setOnDroidSpeechListener(this)
        droidSpeech!!.setShowRecognitionProgressView(false)
        droidSpeech!!.setOneStepResultVerify(false)

        //bouton detection vocale
        startSpeech = findViewById(R.id.virtualStartButton)
        startSpeech?.setOnClickListener(this)

        stopSpeech = findViewById(R.id.virtualStopButton)
        stopSpeech?.setOnClickListener(this)

        //Let's start listening
        //Initiation de l'??coute
        startSpeech?.performClick()

        //camera
        if (checkCameraHardware(this))
            camera=getCameraInstance()
        if (camera!=null)
        {
            Log.i(TAGCamera, "Initialize camera")
            preview = camera?.let {
                // Create our Preview view
                CameraPreview(this, it)
            }
            preview?.also {
                val preview: FrameLayout = findViewById(R.id.camera_preview)
                preview.addView(it)
                Log.i(TAGCamera, "Initialize preview")
            }
        }
    }

    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    private fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            Toast.makeText(this@VisualisationActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            null // returns null if camera is unavailable
        }
    }

    /** Create a file Uri for saving an image or video */
    private fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    /** Create a File for saving an image or video */
    private fun getOutputMediaFile(type: Int): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "ARDesign"
        )
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        mediaStorageDir.apply {
            if (!exists()) {
                if (!mkdirs()) {
                    Log.i(TAGCamera, "failed to create directory")
                    return null
                }
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return when (type) {
            MEDIA_TYPE_IMAGE -> {
                File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")
            }
            MEDIA_TYPE_VIDEO -> {
                File("${mediaStorageDir.path}${File.separator}VID_$timeStamp.mp4")
            }
            else -> null
        }
    }

    private fun getImage()
    {
        val mPicture = Camera.PictureCallback { data, _ ->
            val pictureFile: File = getOutputMediaFile(MEDIA_TYPE_IMAGE) ?: run {
                Log.i(TAGCamera, ("Error creating media file, check storage permissions"))
                return@PictureCallback
            }
            try {
                val fos = FileOutputStream(pictureFile)
                fos.write(data)
                fos.close()
            } catch (e: FileNotFoundException) {
                Log.i(TAGCamera, "File not found: ${e.message}")
            } catch (e: IOException) {
                Log.i(TAGCamera, "Error accessing file: ${e.message}")
            }
        }
        try {
            camera?.takePicture(null, null, mPicture)
            Log.i(TAGCamera, "Take photo")
        }catch (e: Exception)
        {
            Log.i(TAGCamera, "Error taking photo: ${e.message}")
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.capture_button ->{
                getImage()
            }
            R.id.virtualStartButton -> {

                // Starting droid speech
                // D??marrage de droid speech

                //displayDroidSpeech.setContinuousSpeechRecognition(true);
                droidSpeech?.startDroidSpeechRecognition();

                // Setting the view visibilities when droid speech is running
                // D??finir les visibilit?? des vues quand droid speech est en marche
                Toast.makeText(this@VisualisationActivity, "click sur btn start button", Toast.LENGTH_SHORT).show()
                startSpeech?.setVisibility(View.GONE);
                stopSpeech?.setVisibility(View.INVISIBLE);
            }
            R.id.virtualStopButton-> {

                // Closing droid speech
                // Fermeture de droid speech
                droidSpeech?.closeDroidSpeechOperations();
                Toast.makeText(this@VisualisationActivity, "click sur btn stop button", Toast.LENGTH_SHORT).show()

                // Setting the view visibilities when droid speech is running
                // D??finir les visibilit?? des vues quand droid speech est en marche
                stopSpeech?.setVisibility(View.GONE);
                //startSpeech?.setVisibility(View.INVISIBLE);

            }
        }
    }

    override fun onDroidSpeechFinalResult(finalSpeechResult: String) {
        // Setting the final speech result
        //Possibilit?? de modifier les mots-cl??s
        //D??finir un comportement pour chaque mot-cl??
        if (finalSpeechResult.equals("Photo", ignoreCase = true)
            || finalSpeechResult.toLowerCase().contains("photo")
        ) {
            Toast.makeText(this@VisualisationActivity, "final result:photo", Toast.LENGTH_SHORT).show()
            btnPrendrePhoto?.performClick()
            stopSpeech?.performClick()
            //startSpeech.performClick();
        }
        if (finalSpeechResult.equals("Video", ignoreCase = true)
            || finalSpeechResult.toLowerCase().contains("video")
        ) {
            Toast.makeText(this@VisualisationActivity, "final result: video", Toast.LENGTH_SHORT).show()
            btnPrendrePhoto?.performClick()
            stopSpeech?.performClick()
            //startSpeech.performClick();
        }
        if (finalSpeechResult.equals("Start", ignoreCase = true)
            || finalSpeechResult.toLowerCase().contains("video")
        ){
            Toast.makeText(this@VisualisationActivity, "TRY AGAIN", Toast.LENGTH_SHORT).show()

        }
    }





    override fun onDroidSpeechSupportedLanguages(
        currentSpeechLanguage: String?,
        supportedSpeechLanguages: MutableList<String>?
    ) {
        Log.i(TAG, "Supported speech languages = " + supportedSpeechLanguages.toString());
        if (supportedSpeechLanguages != null) {
            if(supportedSpeechLanguages.contains("fr-FR"))
            {
                // Setting the droid speech preferred language as french
                // D??finir la langue pr??f??r??e du discours de droid speech en fran??ais

                droidSpeech?.setPreferredLanguage("fr-FR");
            }
        }
        Log.i(TAG, "Current speech language = " + currentSpeechLanguage);
    }

    override fun onDroidSpeechRmsChanged(rmsChangedValue: Float) {

        // Permet de visualiser des valeurs en nombre ?? chaque tonalit??/ fr??quence de la voix d??t??ct??
        Log.i(TAG, "Rms change value = $rmsChangedValue")
        //lastTimeWorking = System.currentTimeMillis()
    }

    override fun onDroidSpeechLiveResult(liveSpeechResult: String) {
        // Permet de visualiser le mot d??t??ct?? pr??definit
        Log.i(TAG, "Live speech result = $liveSpeechResult")
    }


    override fun onDroidSpeechClosedByUser() {
        //Permet de fermer Droid Speech
        stopSpeech?.setVisibility(View.GONE)
        startSpeech?.setVisibility(View.INVISIBLE)
    }

    override fun onDroidSpeechError(errorMsg: String) {
        // Speech error
        // Permet d'afficher s'il y a une erreur
        //Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
        Log.i(TAG, "Error $errorMsg")
        if (errorMsg.toLowerCase().contains("internet")) {
            internetEnabled = false
        }
        stopSpeech?.post(Runnable { // Stop listening
            stopSpeech?.performClick()
        })
    }

    override fun onDroidSpeechAudioPermissionStatus(
        audioPermissionGiven: Boolean,
        errorMsgIfAny: String?
    ) {
        if (audioPermissionGiven) {
            startSpeech?.post(Runnable { // Start listening
                startSpeech?.performClick()
            })
        } else {
            if (errorMsgIfAny != null) {
                // Permissions error
                Toast.makeText(this, errorMsgIfAny, Toast.LENGTH_LONG).show()
            }
            stopSpeech?.post(Runnable { // Stop listening
                stopSpeech?.performClick()
            })
        }
    }




}