package com.example.listedobjets.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ec.ardesignkitkat.R
//import com.example.listedobjets.R
import com.ec.ardesignkitkat.data.model.Furniture
import java.util.ArrayList

class ObjetAdapter(val c: Context, val objetList:ArrayList<Furniture>) : RecyclerView.Adapter<ObjetAdapter.ObjetViewHolder>(){

    inner class ObjetViewHolder(val v: View) : RecyclerView.ViewHolder(v){
        val nomObjet = v.findViewById<TextView>(R.id.mTitle)
        val descObjet = v.findViewById<TextView>(R.id.mSubTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_item, parent, false)
        return ObjetViewHolder(v)
    }

    override fun getItemCount(): Int {
        return objetList.size
    }

    override fun onBindViewHolder(holder: ObjetViewHolder, position: Int) {
        val newList =  objetList[position]
        holder.nomObjet.text = newList.nomObjet
        holder.descObjet.text = newList.descObjet
    }
}