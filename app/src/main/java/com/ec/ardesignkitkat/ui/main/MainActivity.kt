package com.ec.ardesignkitkat.ui.main


import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.ec.ardesignkitkat.R
import com.ec.ardesignkitkat.data.UserRepository
import com.ec.ardesignkitkat.data.source.remote.SessionManager
import com.vikramezhil.droidspeech.DroidSpeech
import com.vikramezhil.droidspeech.OnDSListener
import com.vikramezhil.droidspeech.OnDSPermissionsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

//login
class MainActivity :AppCompatActivity(), View.OnClickListener, OnDSListener,
    OnDSPermissionsListener {

    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sp: SharedPreferences

    private var pseudo: EditText?= null
    private var mdp: EditText?= null
    private var btnOK: Button?= null

    private var btnEnregistrer: TextView?= null
    private var btnInvite: TextView?= null

    private lateinit var sessionManager: SessionManager

    private var startSpeech: Button? = null
    private var stopSpeech: Button? = null
    private var droidSpeech: DroidSpeech?= null

    private var click: Int = 0
    private var internetEnabled = true
    private var bugTimeCheckHandler: Handler? = null
    private var timeCheckRunnable: Runnable? = null
    private var lastTimeWorking: Long? = null

    var TAG = "ARDesign main"
    private val TIME_RECHECK_DELAY: Int = 5000
    private val TIME_OUT_DELAY: Int = 4000

    val userRepository by lazy { UserRepository.newInstance(application) }

    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
        sessionManager = SessionManager(application)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)

    }

    fun initialize(){
        pseudo = findViewById(R.id.EditUser)
        mdp = findViewById(R.id.EditMdp)
        btnOK = findViewById(R.id.buttonOk)
        btnEnregistrer= findViewById(R.id.Enregistrer)
        btnInvite = findViewById(R.id.Invite)

        btnOK!!.setOnClickListener(this)
        btnEnregistrer!!.setOnClickListener(this)
        btnInvite!!.setOnClickListener(this)

        //*** Bug detection handlers
        //Permet de détécter les bugs si le listener ne répond pas dans un délai précis et de faire une ré-activation du listener pour continuer la détéction
        //*** Bug detection handlers
        //Permet de détécter les bugs si le listener ne répond pas dans un délai précis et de faire une ré-activation du listener pour continuer la détéction


        droidSpeech = DroidSpeech(this, null)
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

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.buttonOk ->
            {
                val pseudoTxt = pseudo?.text.toString()
                val mdpTxt = mdp?.text.toString()
                if(pseudoTxt.isEmpty() or mdpTxt.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity, "Data is missing", Toast.LENGTH_LONG
                    ).show()
                }
                else{
                    login(pseudoTxt, mdpTxt)
                }
            }
            R.id.Enregistrer ->
            {
                val versCreerCompte = Intent(this, CreationCompteActivity::class.java)
                startActivity(versCreerCompte)
            }
            R.id.Invite ->
            {
                versAccueil()
            }
            R.id.virtualStartButton -> {

                // Starting droid speech
                // Démarrage de droid speech

                //displayDroidSpeech.setContinuousSpeechRecognition(true)
                droidSpeech?.startDroidSpeechRecognition()

                // Setting the view visibilities when droid speech is running
                // Définir les visibilité des vues quand droid speech est en marche
                //Toast.makeText(this@MainActivity, "click sur btn start button", Toast.LENGTH_SHORT).show()
                startSpeech?.setVisibility(View.GONE)
                stopSpeech?.setVisibility(View.INVISIBLE)
            }
            R.id.virtualStopButton-> {

                // Closing droid speech
                // Fermeture de droid speech
                droidSpeech?.closeDroidSpeechOperations()
                //Toast.makeText(this@MainActivity, "click sur btn stop button", Toast.LENGTH_SHORT).show()

                // Setting the view visibilities when droid speech is running
                // Définir les visibilité des vues quand droid speech est en marche
                stopSpeech?.setVisibility(View.GONE)
                //startSpeech?.setVisibility(View.INVISIBLE)

            }
        }
    }

    fun login(ps: String, mdp:String){

        activityScope.launch {
            try {
                Log.v(TAG,"function login")
                val connexion = userRepository.connexion(ps,mdp)
                val hash = connexion.hash
                Log.v(TAG, connexion.toString())
                Log.v(TAG, hash)
                //Toast.makeText(this@MainActivity, hash, Toast.LENGTH_SHORT).show()
                if(hash!=null)
                {
                    Log.v(TAG,"HASH NOT NULL")
                    val id_user = connexion.id.toString()
                    val pseudo_user = connexion.pseudo
                    //Garder dans shared preferences
                    //editor.putString("login", ps)
                    //editor.commit()
                    //val l=sp.getString("login","null")
                    //pseudo?.setText(l.toString())
                    val versAccueil: Intent= Intent(this@MainActivity, AccueilActivity::class.java)
                    //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    versAccueil.putExtra("hash", hash )
                    versAccueil.putExtra("id_user", id_user )
                    versAccueil.putExtra("pseudo_user", pseudo_user )
                    startActivity(versAccueil)
                }
                else
                    Toast.makeText(this@MainActivity, "Pseudo ou MDP incorrects", Toast.LENGTH_SHORT).show()
            }
            catch (e:Exception)
            {
                Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun versAccueil(){
        activityScope.launch {
            try {
                val versAccueil = Intent(this@MainActivity, AccueilActivity::class.java)
                //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                //versAccueil.putExtra("hash",  )
                startActivity(versAccueil)
            }
            catch (e:Exception)
            {
                Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDroidSpeechFinalResult(finalSpeechResult: String) {
        // Setting the final speech result
        //Possibilité de modifier les mots-clés
        //Définir un comportement pour chaque mot-clé
        if (finalSpeechResult.equals("Commencer", ignoreCase = true)
            || finalSpeechResult.toLowerCase().contains("commencer")
        ) {
            Toast.makeText(this@MainActivity, "final result: commencer", Toast.LENGTH_SHORT).show()
            btnInvite?.performClick()
            stopSpeech?.performClick()
        }
    }


    override fun onDroidSpeechSupportedLanguages(
        currentSpeechLanguage: String?,
        supportedSpeechLanguages: MutableList<String>?
    ) {
        Log.v(TAG, "Supported speech languages = " + supportedSpeechLanguages.toString());
        if (supportedSpeechLanguages != null) {
            if(supportedSpeechLanguages.contains("fr-FR"))
            {
                // Setting the droid speech preferred language as french
                // Définir la langue préférée du discours de droid speech en français

                droidSpeech?.setPreferredLanguage("fr-FR");
            }
        }
        Log.v(TAG, "Current speech language = " + currentSpeechLanguage);
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
is MainViewModel.ViewState.Content ->{
                    Toast.makeText(this@MainActivity, "yes", Toast.LENGTH_SHORT)
                        .show()
                }
                is MainViewModel.ViewState.Error -> {
                    Toast.makeText(this@MainActivity, "${viewState.message} ", Toast.LENGTH_SHORT)
                        .show()
                }
 */