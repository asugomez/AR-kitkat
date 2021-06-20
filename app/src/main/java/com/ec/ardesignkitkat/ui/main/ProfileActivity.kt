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

    private lateinit var sp: SharedPreferences
    private lateinit var sessionManager: SessionManager
    private val userRepository by lazy { UserRepository.newInstance(application) }
    private val remoteDataProvider = RemoteDataProvider()

    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val hash = sessionManager.fetchAuthToken()
        val l = sp.getString("login","null")
        val pseudo = findViewById<TextView>(R.id.pseudo)
        val mail = findViewById<TextView>(R.id.mail)
        val mdp = findViewById<TextView>(R.id.mdp)

        activityScope.launch {
            try {
                val uid = remoteDataProvider.getUserId(hash!!, l!!)
                val user = userRepository.getUserData(uid, hash)
                pseudo.text = user.pseudo
                mail.text = user.mail
                mdp.text = user.pass

            } catch (e: Exception) {
                alerter(e.message.toString())
            }
        }

    }

    private fun alerter(s: String) {
        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        Log.i(CAT, s)
        t.show()
    }

    companion object {
        private const val CAT = "AR"
    }

}