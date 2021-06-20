package com.ec.ardesignkitkat.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ec.ardesignkitkat.R
import com.vikramezhil.droidspeech.DroidSpeech
import com.vikramezhil.droidspeech.OnDSListener
import com.vikramezhil.droidspeech.OnDSPermissionsListener


class AccueilActivity : AppCompatActivity(), View.OnClickListener, OnDSListener,
    OnDSPermissionsListener {
    private var startSpeech: Button? = null
    private var stopSpeech: Button? = null
    private var btnMesure: Button? = null
    private var btnVisualisation: Button? = null
    private var droidSpeech: DroidSpeech?= null
    private var click: Int = 0
    private var internetEnabled = true;

    var TAG = "DroidSpeech 3"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accueil)
        initialize()
        //ActivityCompat.requestPermissions(this, Manifest.permission.RECORD_AUDIO, 1)
    }

    fun initialize(){
        // bouton Mesure
        btnMesure = findViewById(R.id.mesure_btn)
        btnMesure?.setOnClickListener(this)



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
        //Initiation de l'écoute
        startSpeech?.performClick()


    }

    //Méthode permettant de passer à l'activity qui va permettre d'ouvrir la caméra
    fun openCamera() {
        val cameraIntent = Intent(this, VisualisationActivity::class.java)
        cameraIntent.putExtra("clickValue", click)
        // TODO: add tts_value putextra
        startActivity(cameraIntent)
    }


    /**
     * Gestion des boutons
     */
    override fun onClick(v: View?) {
        when (v!!.id) {
            /*R.id.mesure_btn -> {                // vers mesure activity
                val intent = Intent(this, MesureActivity::class.java)
                // .apply { putExtra(EXTRA_MESSAGE, "msg")}
                startActivity(intent)
            }*/
            R.id.visualisation_btn -> {          // vers visualisation activity
                val intent = Intent(this, VisualisationActivity::class.java)
                // .apply { putExtra(EXTRA_MESSAGE, "msg")}
                startActivity(intent)
            }
            R.id.virtualStartButton -> {

                // Starting droid speech
                // Démarrage de droid speech

                //displayDroidSpeech.setContinuousSpeechRecognition(true);
                droidSpeech?.startDroidSpeechRecognition();

                // Setting the view visibilities when droid speech is running
                // Définir les visibilité des vues quand droid speech est en marche
                startSpeech?.setVisibility(View.GONE);
                stopSpeech?.setVisibility(View.INVISIBLE);
            }
            R.id.virtualStopButton-> {

                // Closing droid speech
                // Fermeture de droid speech
                droidSpeech?.closeDroidSpeechOperations();

                // Setting the view visibilities when droid speech is running
                // Définir les visibilité des vues quand droid speech est en marche
                stopSpeech?.setVisibility(View.GONE);
                startSpeech?.setVisibility(View.INVISIBLE);

            }
        }

    }

    override fun onDroidSpeechFinalResult(finalSpeechResult: String) {
        // Setting the final speech result
        //Possibilité de modifier les mots-clés
        //Définir un comportement pour chaque mot-clé
        if (finalSpeechResult.equals("Visualiser", ignoreCase = true)
            || finalSpeechResult.toLowerCase().contains("visualiser")
        ) {
            //openCamera()
            stopSpeech?.performClick()
            btnVisualisation?.performClick()
            //startSpeech.performClick();
        }

        if (finalSpeechResult.equals("Mesurer", ignoreCase = true)
            || finalSpeechResult.toLowerCase().contains("mesurer")
        ) {
            //openCamera()
            stopSpeech?.performClick()
            btnMesure?.performClick()
            //startSpeech.performClick();
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
                // Définir la langue préférée du discours de droid speech en français

                droidSpeech?.setPreferredLanguage("fr-FR");
            }
        }
        Log.i(TAG, "Current speech language = " + currentSpeechLanguage);
    }

    override fun onDroidSpeechRmsChanged(rmsChangedValue: Float) {

        // Permet de visualiser des valeurs en nombre à chaque tonalité/ fréquence de la voix détécté
        Log.i(TAG, "Rms change value = $rmsChangedValue")
        //lastTimeWorking = System.currentTimeMillis()
    }

    override fun onDroidSpeechLiveResult(liveSpeechResult: String) {
        // Permet de visualiser le mot détécté prédefinit
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