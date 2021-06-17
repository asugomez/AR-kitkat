package com.ec.ardesignkitkat.ui.main

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ec.ardesignkitkat.R
//login
class SaveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)
    }

    fun prendrePhoto(view: View) {

        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Sauvegarder")
        alertDialog.setMessage("Voulez-vous sauvegarder ces dimensions ?")

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Oui"
        ) { dialog, which -> dialog.cancel() }

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Non"
        ) { dialog, which -> dialog.dismiss() }
        alertDialog.show()

        val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)


        btnPositive.setOnClickListener {
            withEditText(it)
            alertDialog.cancel()
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
        builder.setTitle("Nom Objet")
        val dialogLayout = inflater.inflate(R.layout.nom_objet, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Sauvegarder") { dialogInterface, i -> Toast.makeText(applicationContext, "Nom sauvegardé :" + editText.text.toString(), Toast.LENGTH_SHORT).show() }
        builder.show()
    }

}
