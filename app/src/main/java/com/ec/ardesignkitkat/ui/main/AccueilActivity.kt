package com.ec.ardesignkitkat.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ec.ardesignkitkat.R
import com.google.ar.core.ArCoreApk


class AccueilActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var pseudo: String
    private lateinit var btnMesure: Button
    private lateinit var btnVisualisation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accueil)

        // bouton Mesure
        btnMesure = findViewById(R.id.mesure_btn)
        btnMesure.setOnClickListener(this)

        // bouton Visualisation
        btnVisualisation = findViewById(R.id.visualisation_btn)
        btnMesure.setOnClickListener(this)

        val bdl = this.intent.extras
        pseudo = bdl!!.getString("pseudo")!!

        // Enable AR-related functionality on ARCore supported devices only.
        maybeEnableArButton()

    }

    private fun maybeEnableArButton() {
        val availability = ArCoreApk.getInstance().checkAvailability(this)
        if (availability.isTransient) {
            // Continue to query availability at 5Hz while compatibility is checked in the background.
            Handler().postDelayed({
                maybeEnableArButton()
            }, 200)
        }
        if (availability.isSupported) {
            btnMesure.isEnabled = true
            btnVisualisation.isEnabled = true
        } else { // The device is unsupported or unknown.
            btnMesure.isEnabled = false
            btnVisualisation.isEnabled = false
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
                startActivity(iProfil)
            }
            R.id.menu_objets -> {
                //TODO Page des objets
//                val iObjets = Intent(this, ::class.java)
//                startActivity(iObjets)
            }
            R.id.menu_logout -> {
                val iLogin = Intent(this, MainActivity::class.java)
                startActivity(iLogin)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Gestion des boutons
     */
    override fun onClick(v: View?) {

        /*
        when (v!!.id) {
            R.id.mesure_btn ->                  // vers mesure activity
                val intent = Intent(this, MesureActivity::class.java)
                // .apply { putExtra(EXTRA_MESSAGE, "msg")}
                startActivity(intent)
            R.id.visualisation_btn ->           // vers visualisation activity
                val intent = Intent(this, VisualisationActivity::class.java)
                // .apply { putExtra(EXTRA_MESSAGE, "msg")}
                startActivity(intent)
        }

         */

        when (v!!.id) {
            R.id.mesure_btn -> startActivity(Intent(this, MesureActivity::class.java))
            //R.id.visualisation_btn -> startActivity(Intent(this, VisualisationActivity::class.java))
        }

    }

}