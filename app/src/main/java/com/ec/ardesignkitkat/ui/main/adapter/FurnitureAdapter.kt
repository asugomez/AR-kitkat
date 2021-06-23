package com.ec.ardesignkitkat.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ec.ardesignkitkat.R
import com.ec.ardesignkitkat.data.model.Furniture

class FurnitureAdapter(private val dataset: MutableList<Furniture>): RecyclerView.Adapter<FurnitureAdapter.FurnViewHolder>() {

    private var mOnItemClickListener: AdapterView.OnItemClickListener? = null
    //private val furnitures: MutableList<Furniture> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FurnViewHolder(itemView = inflater.inflate(R.layout.list_item, parent, false))
    }


    override fun onBindViewHolder(holder: FurnViewHolder, position: Int) {
        holder.bind((dataset[position]))
        holder.itemView.setOnClickListener{

        }

        holder.itemView.setOnClickListener {
            if (mOnItemClickListener != null) mOnItemClickListener!!.onItemClick(position)
            //Type mismatch: inferred type is Int but AdapterView<*>! was expected
        }
    }

    override fun getItemCount() = dataset.size

    fun addData(newDataSet: List<Furniture>) {
        //dataset.clear()
        dataset.addAll(newDataSet)
        notifyItemChanged(dataset.size)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    class FurnViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.mTitle)
        val subtitle = itemView.findViewById<TextView>(R.id.mSubTitle)
        val btn = itemView.findViewById<Button>(R.id.btnPartager)

        //val image= itemView.findViewById<TextView>(R.id.imageObject)

        fun bind(furniture: Furniture) {
            title.text = furniture.nom
            val l = furniture.length
            val w = furniture.width
            val h = furniture.height
            subtitle.text = "$l x $w x $h"

        }
    }

    interface OnItemClickListener : AdapterView.OnItemClickListener {
        fun onItemClick(position: Int)

    }
}

private fun Any.onItemClick(position: Int) {

}



