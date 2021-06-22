package com.ec.ardesignkitkat.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ec.ardesignkitkat.R
import com.ec.ardesignkitkat.data.model.Furniture
import com.example.listedobjets.view.ObjetAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MesObjetsActivity(
    val id: String,
    val idUser: String,
    val width: String,
    val height: String,
    val length: String
) : AppCompatActivity() {
    private lateinit var addsBtn: FloatingActionButton
    private lateinit var recv: RecyclerView
    private lateinit var objetList: ArrayList<Furniture>
    private lateinit var objetAdapter: ObjetAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mes_objets)

        objetList = ArrayList()
        addsBtn = findViewById(R.id.addingBtn)
        recv = findViewById(R.id.mRecycler)
        objetAdapter = ObjetAdapter(this,objetList)
        recv.layoutManager = LinearLayoutManager(this)
        recv.adapter = objetAdapter
        addsBtn.setOnClickListener { addInfo() }

    }


    private fun addInfo() {
        val inflter = LayoutInflater.from(this)
        val v = inflter.inflate(R.layout.add_objet,null)
        val nomObjet = v.findViewById<EditText>(R.id.nomObjet)
        val descObjet = v.findViewById<EditText>(R.id.descObjet)
        val addDialog = AlertDialog.Builder(this)
        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
            dialog,_->
            val noms = nomObjet.text.toString()
            val descriptions = descObjet.text.toString()
            objetList.add(Furniture("Id: $id", idUser = "IdUser : $idUser", width = "Width : $width", height = "Height : $height", length = "Length : $length", nomObjet = "nomObjet: $nomObjet",descObjet = "Description: $descriptions"))
            objetAdapter.notifyDataSetChanged()
            Toast.makeText(this,"Adding Object Name Success",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel"){
            dialog,_->
            dialog.dismiss()
            Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show()
        }
        addDialog.create()
        addDialog.show()
    }

}