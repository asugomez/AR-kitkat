package com.ec.ardesignkitkat.ui.main

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ec.ardesignkitkat.R


class AccueilActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var pseudo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accueil)

        // bouton Mesure
        val btnMesure = findViewById<Button>(R.id.mesure_btn)
        btnMesure.setOnClickListener(this)

        // bouton Visualisation
        val btnVisualisation = findViewById<Button>(R.id.mesure_btn)
        btnMesure.setOnClickListener(this)

        val bdl = this.intent.extras
        pseudo = bdl!!.getString("pseudo")!!

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
            //R.id.mesure_btn -> startActivity(Intent(this, MesureActivity::class.java))
            //R.id.visualisation_btn -> startActivity(Intent(this, VisualisationActivity::class.java))
        }

    }

}