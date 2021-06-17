package com.ec.ardesignkitkat.ui.main


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ec.ardesignkitkat.R
import com.ec.ardesignkitkat.data.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

//login
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sp: SharedPreferences

    private var pseudo: EditText?= null
    private var mdp: EditText?= null
    private var btnOK: Button?= null

    private var btnEnregistrer: TextView?= null
    private var btnInvite: TextView?= null

    val userRepository by lazy { UserRepository.newInstance(application) }

    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
        btnOK!!.setOnClickListener(this)
        btnEnregistrer!!.setOnClickListener(this)
        btnInvite!!.setOnClickListener(this)
    }

    fun initialize(){
        pseudo = findViewById(R.id.EditUser)
        mdp = findViewById(R.id.EditMdp)
        btnOK = findViewById(R.id.buttonOk)
        btnEnregistrer= findViewById(R.id.Enregistrer)
        btnInvite = findViewById(R.id.Invite)

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
        }
    }

    fun login(ps: String, mdp:String){
        activityScope.launch {
            try {
                val hash = userRepository.connexion(ps,mdp)
                if(hash!=null)
                {
                    //Garder dans shared preferences
                    editor.putString("login", ps)
                    editor.commit()
                    val l=sp.getString("login","null")
                    pseudo?.setText(l.toString())
                    val versAccueil: Intent= Intent(this@MainActivity, AccueilActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    //versAccueil.putExtra("hash",  )
                    startActivity(versAccueil)
                }
                else
                    Toast.makeText(this@MainActivity, "Erreur de Connection", Toast.LENGTH_SHORT).show()
            }
            catch (e:Exception)
            {
                Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }
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