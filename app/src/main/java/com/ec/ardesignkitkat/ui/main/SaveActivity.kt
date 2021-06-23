package com.ec.ardesignkitkat.ui.main

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ec.ardesignkitkat.R
import com.ec.ardesignkitkat.data.FurnitureRepository
import com.vikramezhil.droidspeech.DroidSpeech
import com.vikramezhil.droidspeech.OnDSListener
import com.vikramezhil.droidspeech.OnDSPermissionsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

//login
class SaveActivity : AppCompatActivity(),View.OnClickListener,OnDSListener,OnDSPermissionsListener {


    private var startSpeech: Button? = null
    private var stopSpeech: Button? = null
    private var droidSpeech: DroidSpeech?= null
    private var hash: String? = null
    private var id_user: Int? = null

    private var btnCapture: Button? = null

    private var alertDialog: AlertDialog? =null


    var TAG = "DroidSpeech 3"

    private var internetEnabled = true;

    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
    )

    val furnitureRepository by lazy { FurnitureRepository.newInstance(application) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)
        initialize()
    }

    fun initialize(){
        // todo: changer
        hash = "12d89521405e3032b39368f7b8801b24"
        id_user = 1

        btnCapture = findViewById(R.id.capture)
        btnCapture?.setOnClickListener(this)

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
    }



    fun prendrePhoto() {
        alertDialog = AlertDialog.Builder(this).create()
        alertDialog!!.setTitle("Sauvegarder")
        alertDialog!!.setMessage("Voulez-vous sauvegarder ces dimensions ?")

        alertDialog!!.setButton(AlertDialog.BUTTON_POSITIVE, "Oui"
        ) { dialog, which -> dialog.cancel() }

        alertDialog!!.setButton(AlertDialog.BUTTON_NEGATIVE, "Non"
        ) { dialog, which -> dialog.dismiss() }
        alertDialog!!.show()

        val btnPositive = alertDialog!!.getButton(AlertDialog.BUTTON_POSITIVE)
        val btnNegative = alertDialog!!.getButton(AlertDialog.BUTTON_NEGATIVE)


        btnPositive.setOnClickListener {
            withEditText(it)
            alertDialog!!.cancel()
        }

        btnNegative.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        }

        val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 10f
        btnPositive.layoutParams = layoutParams
        btnNegative.layoutParams = layoutParams
    }

    fun withEditText(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        //builder.setTitle("Nom Objet")
        //val dialogLayout = inflater.inflate(R.layout.nom_objet, null)
        val dialogLayout = inflater.inflate(R.layout.add_objet, null)
        //val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
        val nom_object = dialogLayout.findViewById<EditText>(R.id.nomObjet)
        val l_object = dialogLayout.findViewById<EditText>(R.id.lObject)
        val w_object = dialogLayout.findViewById<EditText>(R.id.wObject)
        val h_object = dialogLayout.findViewById<EditText>(R.id.hObject)
        val btnSauvegarder = dialogLayout.findViewById<Button>(R.id.btnSauvegarder)
        builder.setView(dialogLayout)
        btnSauvegarder.setOnClickListener{
            sauvergarder(nom_object.text.toString(), l_object.text.toString(), w_object.text.toString(), h_object.text.toString())
        }
        /*builder.setPositiveButton("Sauvegarder") {
                dialogInterface, i ->
            Toast.makeText(applicationContext, "Nom sauvegardé :" + nom_object.text.toString(), Toast.LENGTH_SHORT).show()
        }*/
        builder.show()

    }

    fun sauvergarder(nom:String, length: String, width: String, height: String){
        activityScope.launch{
            try{
                if(hash!=null){
                    furnitureRepository.addUsersFurniture(id_user!!, width, height, length,nom, hash!!)
                }
            } catch (e: Exception){
                Toast.makeText(this@SaveActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onClick(v: View?)
    {
        when(v!!.id)
        {
            R.id.virtualStartButton -> {

                // Starting droid speech
                // Démarrage de droid speech

                //displayDroidSpeech.setContinuousSpeechRecognition(true);
                droidSpeech?.startDroidSpeechRecognition();

                // Setting the view visibilities when droid speech is running
                // Définir les visibilité des vues quand droid speech est en marche
                Toast.makeText(this@SaveActivity, "click sur btn start button", Toast.LENGTH_SHORT).show()
                startSpeech?.setVisibility(View.GONE);
                stopSpeech?.setVisibility(View.INVISIBLE);
            }
            R.id.virtualStopButton-> {

                // Closing droid speech
                // Fermeture de droid speech
                droidSpeech?.closeDroidSpeechOperations();
                Toast.makeText(this@SaveActivity, "click sur btn stop button", Toast.LENGTH_SHORT).show()

                // Setting the view visibilities when droid speech is running
                // Définir les visibilité des vues quand droid speech est en marche
                stopSpeech?.setVisibility(View.GONE);
                //startSpeech?.setVisibility(View.INVISIBLE);

            }
            AlertDialog.BUTTON_POSITIVE->
            {
                withEditText(v)
                alertDialog?.cancel()
            }
            AlertDialog.BUTTON_NEGATIVE->
            {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            }

            R.id.capture ->
            {
                prendrePhoto()
            }
        }
    }

    override fun onDroidSpeechFinalResult(finalSpeechResult: String?) {
        if (finalSpeechResult != null) {
            if (finalSpeechResult.equals("Sauvegarder", ignoreCase = true)
                || finalSpeechResult.toLowerCase().contains("sauvegarder")
            ) {
                //Toast.makeText(this@AccueilActivity, "final result: visualiser", Toast.LENGTH_SHORT).show()
                //openCamera()
                prendrePhoto()
                stopSpeech?.performClick()
                //startSpeech.performClick();
            }
        }
        if (finalSpeechResult != null) {
            if (finalSpeechResult.equals("Oui", ignoreCase = true)
                || finalSpeechResult.toLowerCase().contains("oui")
            ) {

                stopSpeech?.performClick()

            }
        }
        if (finalSpeechResult != null) {
            if (finalSpeechResult.equals("Non", ignoreCase = true)
                || finalSpeechResult.toLowerCase().contains("non")
            ) {

                stopSpeech?.performClick()

            }
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

    override fun onDroidSpeechLiveResult(liveSpeechResult: String?) {
        // Permet de visualiser le mot détécté prédefinit
        Log.i(TAG, "Live speech result = $liveSpeechResult")
    }

    override fun onDroidSpeechClosedByUser() {
        //Permet de fermer Droid Speech
        stopSpeech?.setVisibility(View.GONE)
        startSpeech?.setVisibility(View.INVISIBLE)
    }

    override fun onDroidSpeechError(errorMsg: String?) {
        // Speech error
        // Permet d'afficher s'il y a une erreur
        //Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
        Log.i(TAG, "Error $errorMsg")
        if (errorMsg != null) {
            if (errorMsg.toLowerCase().contains("internet")) {
                internetEnabled = false
            }
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
