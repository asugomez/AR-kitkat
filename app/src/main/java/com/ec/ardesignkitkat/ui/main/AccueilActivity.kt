package com.ec.ardesignkitkat.ui.main

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.ec.ardesignkitkat.R
import com.ec.ardesignkitkat.ui.main.mesure.helloar.HelloArActivity
import com.google.ar.core.ArCoreApk
import com.vikramezhil.droidspeech.DroidSpeech
import com.vikramezhil.droidspeech.OnDSListener
import com.vikramezhil.droidspeech.OnDSPermissionsListener


class AccueilActivity : AppCompatActivity(), View.OnClickListener, OnDSListener,
    OnDSPermissionsListener {
    private var startSpeech: Button? = null
    private var stopSpeech: Button? = null
    private var btnMesure: Button? = null
    private var btnVisualisation: Button? = null
    private var droidSpeechAccueil: DroidSpeech?= null

    private var click: Int = 0
    private var internetEnabled = true;
    private var bugTimeCheckHandler: Handler? = null
    private var timeCheckRunnable: Runnable? = null
    private var lastTimeWorking: Long? = null

    var TAG = "ARDesign accueil"
    private val TIME_RECHECK_DELAY: Int = 5000
    private val TIME_OUT_DELAY: Int = 4000
    
    private var hash: String? = null
    private var id_user: String? = null
    private var pseudo_user: String? = null
    private var mail_user: String? = null
    private var pass: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflatedView: View = layoutInflater.inflate(R.layout.activity_main,null)

        val btnStop = inflatedView.findViewById<Button>(R.id.virtualStopButton)
        val btnStart = inflatedView.findViewById<Button>(R.id.virtualStartButton)
        Log.i(TAG, btnStop.toString())

        btnStop.performClick()
        setContentView(R.layout.accueil)
        //MainActivity.stopSpeech.performClick()
        //val binding: MainActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //Log.i(TAG, btnStop.performClick().toString())
        initialize()
        //*** Bug detection handlers
        //Permet de détécter les bugs si le listener ne répond pas dans un délai précis et de faire une ré-activation du listener pour continuer la détéction

        droidSpeechAccueil = DroidSpeech(this, null)
        droidSpeechAccueil!!.setOnDroidSpeechListener(this)
        droidSpeechAccueil!!.setShowRecognitionProgressView(false)
        droidSpeechAccueil!!.setOneStepResultVerify(false)

        //bouton detection vocale
        startSpeech = findViewById(R.id.virtualStartButton)
        startSpeech?.setOnClickListener(this)

        stopSpeech = findViewById(R.id.virtualStopButton)
        stopSpeech?.setOnClickListener(this)

        //Let's start listening
        //Initiation de l'écoute
        startSpeech?.performClick()

        // Enable AR-related functionality on ARCore supported devices only.
        //maybeEnableArButton()
    }

    fun initialize(){
        // bouton Mesure
        btnMesure = findViewById(R.id.mesure_btn)
        btnMesure?.setOnClickListener(this)

        btnVisualisation?.setOnClickListener(this)
        btnVisualisation = findViewById(R.id.visualisation_btn)
        btnVisualisation?.setOnClickListener(this)

        //recuperationdes données
        hash = intent.getStringExtra("hash")
        id_user = intent.getStringExtra("id_user")
        pseudo_user = intent.getStringExtra("pseudo_user")
        mail_user = intent.getStringExtra("mail_user")
        pass = intent.getStringExtra("pass")

        if(pseudo_user!=null){
            this.title = "Bienvenue $pseudo_user !"
        }else{
            this.title = "Bienvenue !"
        }



    }

    override fun onPause() {
        super.onPause()
        if (stopSpeech?.getVisibility() === View.VISIBLE) {
            stopSpeech?.performClick()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (stopSpeech?.getVisibility() === View.VISIBLE) {
            stopSpeech?.performClick()
        }
    }

    private fun maybeEnableArButton() {
        val availability: ArCoreApk.Availability = ArCoreApk.getInstance().checkAvailability(this)
        if (availability.isTransient) {
            // Continue to query availability at 5Hz while compatibility is checked in the background.
            Handler().postDelayed({
                maybeEnableArButton()
            }, 200)
        }
        if (availability.isSupported) {
            btnMesure!!.isEnabled  = true
            btnVisualisation!!.isEnabled = true
        } else { // The device is unsupported or unknown.
            btnMesure!!.isEnabled  = false
            btnVisualisation!!.isEnabled = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.menu_profil -> {
                val iProfil = Intent(this, ProfileActivity::class.java)
                iProfil.putExtra("hash", hash )
                iProfil.putExtra("id_user", id_user )
                iProfil.putExtra("pseudo_user", pseudo_user )
                iProfil.putExtra("pass", pass )
                iProfil.putExtra("mail", mail_user )
                startActivity(iProfil)
                startSpeech?.performClick()
            }
            R.id.menu_objets -> {
                //Toast.makeText(this@AccueilActivity, "click sur liste mes objets", Toast.LENGTH_SHORT).show()
                val iObjets = Intent(this, ListFurnitureActivity::class.java)
                iObjets.putExtra("hash", hash )
                iObjets.putExtra("id_user", id_user )
                iObjets.putExtra("pseudo_user", pseudo_user )
                startActivity(iObjets)
                startSpeech?.performClick()
            }
            R.id.menu_logout -> {
                val iLogin = Intent(this, MainActivity::class.java)
                hash = null
                startActivity(iLogin)
                startSpeech?.performClick()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Gestion des boutons
     */
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.mesure_btn -> {                // vers mesure activity
                val intent = Intent(this, HelloArActivity::class.java)
                // .apply { putExtra(EXTRA_MESSAGE, "msg")}
                intent.putExtra("hash", hash)
                intent.putExtra("id_user", id_user)
                intent.putExtra("pseudo_user", pseudo_user )
                startActivity(intent)
            }
            R.id.visualisation_btn -> {          // vers visualisation activity
                val intent = Intent(this, VisualisationActivity::class.java)
                // .apply { putExtra(EXTRA_MESSAGE, "msg")}
                startActivity(intent)
            }
            R.id.virtualStartButton -> {

                // Starting droid speech
                // Démarrage de droid speech

                //droid.setContinuousSpeechRecognition(true);
                droidSpeechAccueil?.startDroidSpeechRecognition()

                // Setting the view visibilities when droid speech is running
                // Définir les visibilité des vues quand droid speech est en marche
                //Toast.makeText(this@AccueilActivity, "click sur btn start button", Toast.LENGTH_SHORT).show()
                startSpeech?.setVisibility(View.GONE)
                stopSpeech?.setVisibility(View.INVISIBLE)
            }
            R.id.virtualStopButton-> {

                // Closing droid speech
                // Fermeture de droid speech
                droidSpeechAccueil?.closeDroidSpeechOperations()
                //Toast.makeText(this@AccueilActivity, "click sur btn stop button", Toast.LENGTH_SHORT).show()

                // Setting the view visibilities when droid speech is running
                // Définir les visibilité des vues quand droid speech est en marche
                stopSpeech?.setVisibility(View.GONE)
                startSpeech?.setVisibility(View.INVISIBLE);

            }
        }

    }

    override fun onDroidSpeechFinalResult(finalSpeechResult: String) {
        // Setting the final speech result
        //Possibilité de modifier les mots-clés
        //Définir un comportement pour chaque mot-clé
        Log.i(TAG, finalSpeechResult)

        if (finalSpeechResult.equals("Visualiser", ignoreCase = true)
            || finalSpeechResult.toLowerCase().contains("visualiser")
        ) {
            Toast.makeText(this@AccueilActivity, "Visualiser", Toast.LENGTH_SHORT).show()
            //openCamera()
            btnVisualisation?.performClick()
            startSpeech?.performClick()
        }

        if (finalSpeechResult.equals("Mesurer", ignoreCase = true)
            || finalSpeechResult.toLowerCase().contains("mesurer")
            || finalSpeechResult.toLowerCase().contains("mets")
            || finalSpeechResult.toLowerCase().contains("sur")
        ) {
            Toast.makeText(this@AccueilActivity, "Mesurer", Toast.LENGTH_SHORT).show()
            btnMesure?.performClick()
            startSpeech?.performClick()
            //stopSpeech?.performClick()
            //onDestroy()
        }

        if (finalSpeechResult.equals("Mes objets", ignoreCase = true)
            || finalSpeechResult.toLowerCase().contains("mes objets")
        ) {
            Toast.makeText(this@AccueilActivity, "Mes objets", Toast.LENGTH_SHORT).show()
            val iObjets = Intent(this, ListFurnitureActivity::class.java)
            iObjets.putExtra("hash", hash )
            iObjets.putExtra("id_user", id_user )
            iObjets.putExtra("pseudo_user", pseudo_user )
            startActivity(iObjets)
            startSpeech?.performClick()
            //stopSpeech?.performClick()
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

                droidSpeechAccueil?.setPreferredLanguage("fr-FR");
            }
        }
        Log.i(TAG, "Current speech language = " + currentSpeechLanguage);
    }

    override fun onDroidSpeechRmsChanged(rmsChangedValue: Float) {

        // Permet de visualiser des valeurs en nombre à chaque tonalité/ fréquence de la voix détécté
        //Log.i(TAG, "Rms change value = $rmsChangedValue")
        lastTimeWorking = System.currentTimeMillis()
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
/*
bugTimeCheckHandler = Handler()
        timeCheckRunnable = object : Runnable {
            override fun run() {
                //Log.i(TAG, "Handler 1 running...");
                val timeDifference: Long = System.currentTimeMillis() - lastTimeWorking!!
                if (timeDifference > TIME_OUT_DELAY && internetEnabled) {
                    //*** Do action (restartActivity or restartListening)
                    Log.e(TAG, "Bug Detected ! Restart listener...")
                    stopSpeech!!.performClick()
                    startSpeech!!.performClick()
                }
                bugTimeCheckHandler?.postDelayed(this, TIME_RECHECK_DELAY.toLong())
            }
        }
        bugTimeCheckHandler?.postDelayed(timeCheckRunnable as Runnable, TIME_RECHECK_DELAY.toLong())
 */