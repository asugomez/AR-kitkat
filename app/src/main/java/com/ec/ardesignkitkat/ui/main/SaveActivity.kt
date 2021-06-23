package com.ec.ardesignkitkat.ui.main

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ec.ardesignkitkat.R
import com.ec.ardesignkitkat.data.FurnitureRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

//login
class SaveActivity : AppCompatActivity() {

    private var hash: String? = null
    private var id_user: Int? = null

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

}
