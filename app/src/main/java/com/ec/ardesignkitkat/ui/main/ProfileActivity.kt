package com.ec.ardesignkitkat.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ec.ardesignkitkat.R
import com.ec.ardesignkitkat.data.UserRepository
import com.ec.ardesignkitkat.data.source.remote.RemoteDataProvider
import com.ec.ardesignkitkat.data.source.remote.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ProfileActivity: AppCompatActivity() {

    //private lateinit var sp: SharedPreferences
    //private lateinit var sessionManager: SessionManager

    private var pseudo_user: String? = null
    //private var hash: String? = null
    //private var id_user: String? = null
    private var mail_user: String? = null
    private var pass: String? = null

    private var pseudo: TextView? = null
    private var mail: TextView? = null
    private var mdp: TextView? = null

    private val TAG = "ARDesign profile"

    //private val userRepository by lazy { UserRepository.newInstance(application) }
    //private val remoteDataProvider = RemoteDataProvider()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initialize()
    }

    fun initialize(){
        Log.i(TAG, "function initialize")
        //val l = sp.getString("login","null")
        pseudo = findViewById(R.id.pseudo)
        mail = findViewById(R.id.mail)
        mdp = findViewById(R.id.mdp)

        //hash = intent.getStringExtra("hash")
        //id_user = intent.getStringExtra("id_user")
        pseudo_user = intent.getStringExtra("pseudo_user")
        mail_user = intent.getStringExtra("mail")
        pass = intent.getStringExtra("pass")
        Toast.makeText(this@ProfileActivity, pseudo_user, Toast.LENGTH_SHORT).show()
        Toast.makeText(this@ProfileActivity, mail_user, Toast.LENGTH_SHORT).show()
        Toast.makeText(this@ProfileActivity, pass, Toast.LENGTH_SHORT).show()

        //pseudo =

        pseudo?.text = pseudo_user
        mail?.text = mail_user
        mdp?.text = pass

    }

    /*private fun alerter(s: String) {
        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        Log.i(TAG, s)
        t.show()
    }*/


}