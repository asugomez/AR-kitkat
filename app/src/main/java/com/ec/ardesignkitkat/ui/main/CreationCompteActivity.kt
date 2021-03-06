package com.ec.ardesignkitkat.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ec.ardesignkitkat.R
import com.ec.ardesignkitkat.data.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CreationCompteActivity : AppCompatActivity(), View.OnClickListener {

    private var pseudo: EditText?= null
    private var mdp: EditText?= null
    private var mail: EditText?= null
    private var btn: Button?= null

    val userRepository by lazy { UserRepository.newInstance(application) }

    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_compte)
        initialize()
        btn!!.setOnClickListener(this)
    }

    fun initialize(){
        pseudo = findViewById(R.id.EditUserCreation)
        mdp = findViewById(R.id.EditMdpCreation)
        mail = findViewById(R.id.EditMail)
        btn = findViewById(R.id.buttonCréer)

    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.buttonCréer ->
            {
                if((pseudo?.text.toString()).isEmpty() or (mdp?.text.toString()).isEmpty() or (mail?.text.toString()).isEmpty()) {
                    Toast.makeText(
                        this@CreationCompteActivity, "Data is missing", Toast.LENGTH_LONG
                    ).show()
                }
                else{
                    activityScope.launch {
                        try {
                            if(userRepository.mkUser(pseudo?.text.toString(),mdp?.text.toString(),mail?.text.toString())){
                                versLogin()
                            }
                        }
                        catch (e:Exception)
                        {
                            Toast.makeText(this@CreationCompteActivity, "Erreur dans la création d'utilisateur. Veuillez essayer avec un autre pseudo", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }
    }

    fun versLogin(){
        activityScope.launch {
            try {
                val versLogin = Intent(this@CreationCompteActivity, MainActivity::class.java)
                startActivity(versLogin)
            }
            catch (e:Exception)
            {
                Toast.makeText(this@CreationCompteActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}