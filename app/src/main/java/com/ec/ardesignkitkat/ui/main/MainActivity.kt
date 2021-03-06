package com.ec.ardesignkitkat.ui.main


import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
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

        //*** Bug detection handlers
        //Permet de d??t??cter les bugs si le listener ne r??pond pas dans un d??lai pr??cis et de faire une r??-activation du listener pour continuer la d??t??ction
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)

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
        //Initiation de l'??coute
        startSpeech?.performClick()


    }

    fun initialize(){
        sp = PreferenceManager.getDefaultSharedPreferences(this)
        editor = sp.edit()

        pseudo = findViewById(R.id.EditUser)
        mdp = findViewById(R.id.EditMdp)
        btnOK = findViewById(R.id.buttonOk)
        btnEnregistrer= findViewById(R.id.Enregistrer)
        btnInvite = findViewById(R.id.Invite)

        btnOK!!.setOnClickListener(this)
        btnEnregistrer!!.setOnClickListener(this)
        btnInvite!!.setOnClickListener(this)



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
                    //onPause()
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
                // D??marrage de droid speech

                //displayDroidSpeech.setContinuousSpeechRecognition(true)
                droidSpeech?.startDroidSpeechRecognition()

                // Setting the view visibilities when droid speech is running
                // D??finir les visibilit?? des vues quand droid speech est en marche
                //Toast.makeText(this@MainActivity, "click sur btn start button", Toast.LENGTH_SHORT).show()
                startSpeech?.setVisibility(View.GONE)
                stopSpeech?.setVisibility(View.INVISIBLE)
            }
            R.id.virtualStopButton-> {


                //Toast.makeText(this@MainActivity, "click sur btn stop button", Toast.LENGTH_SHORT).show()

                // Fermeture de droid speech
                Log.i(TAG, droidSpeech?.closeDroidSpeechOperations().toString())
                droidSpeech?.closeDroidSpeechOperations()

                // Setting the view visibilities when droid speech is running
                // D??finir les visibilit?? des vues quand droid speech est en marche
                // Closing droid speech
                Log.i(TAG, "inside r id virtual stop button")
                stopSpeech?.setVisibility(View.GONE)
                startSpeech?.setVisibility(View.INVISIBLE)


            }
        }
    }

    fun login(ps: String, mdp:String){

        activityScope.launch {
            try {
                Log.v(TAG,"function login")
                val connexion = userRepository.connexion(ps,mdp)
                val hash = connexion.hash
                //Toast.makeText(this@MainActivity, hash, Toast.LENGTH_SHORT).show()
                if(hash!=null)
                {
                    Log.v(TAG,"HASH NOT NULL")
                    val id_user = connexion.id.toString()
                    val pseudo_user = connexion.pseudo
                    val mail_user = connexion.mail
                    val pass = connexion.pass
                    //Garder dans shared preferences
                    editor.putString("login", ps)
                    editor.commit()
                    val l=sp.getString("login","null")
                    pseudo?.setText(l.toString())
                    val versAccueil: Intent= Intent(this@MainActivity, AccueilActivity::class.java)
                    //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    versAccueil.putExtra("hash", hash )
                    versAccueil.putExtra("id_user", id_user )
                    versAccueil.putExtra("pseudo_user", pseudo_user )
                    versAccueil.putExtra("mail_user", mail_user )
                    versAccueil.putExtra("pass", pass )
                    startActivity(versAccueil)
                }
                else
                    Toast.makeText(this@MainActivity, "Pseudo ou MDP incorrects", Toast.LENGTH_SHORT).show()
            }
            catch (e:Exception)
            {
                Toast.makeText(this@MainActivity, "Pseudo ou MDP incorrects", Toast.LENGTH_SHORT).show()
                //Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun versAccueil(){
        activityScope.launch {
            try {
                val versAccueil = Intent(this@MainActivity, AccueilActivity::class.java)
                //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                //versAccueil.putExtra("hash",  )
                //onPause()
                startActivity(versAccueil)
            }
            catch (e:Exception)
            {
                Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }
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

    override fun onDroidSpeechFinalResult(finalSpeechResult: String) {
        // Setting the final speech result
        //Possibilit?? de modifier les mots-cl??s
        //D??finir un comportement pour chaque mot-cl??
        if (finalSpeechResult.equals("Commencer", ignoreCase = true)
            || finalSpeechResult.toLowerCase().contains("commencer")
        ) {
            //Toast.makeText(this@MainActivity, "final result: commencer", Toast.LENGTH_SHORT).show()
            btnInvite?.performClick()
            startSpeech?.performClick()

            //onDestroy()
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
                // D??finir la langue pr??f??r??e du discours de droid speech en fran??ais

                droidSpeech?.setPreferredLanguage("fr-FR");

            }
        }
        Log.v(TAG, "Current speech language = " + currentSpeechLanguage);
    }

    override fun onDroidSpeechRmsChanged(rmsChangedValue: Float) {

        // Permet de visualiser des valeurs en nombre ?? chaque tonalit??/ fr??quence de la voix d??t??ct??
        //Log.i(TAG, "Rms change value = $rmsChangedValue")
        lastTimeWorking = System.currentTimeMillis()
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

/*
bugTimeCheckHandler = Handler()
timeCheckRunnable = object : Runnable {
    override fun run() {
        //Log.i(TAG, "Handler 1 running...");
        val timeDifference = System.currentTimeMillis() - lastTimeWorking!!
        if (timeDifference > TIME_OUT_DELAY && internetEnabled) {
            Log.e(TAG, "Bug Detected ! Restart listener...")
            stopSpeech!!.performClick()
            startSpeech!!.performClick()
        }
        bugTimeCheckHandler!!.postDelayed(this, TIME_RECHECK_DELAY.toLong())
    }
}
bugTimeCheckHandler!!.postDelayed(timeCheckRunnable as Runnable, TIME_RECHECK_DELAY.toLong())
*/
