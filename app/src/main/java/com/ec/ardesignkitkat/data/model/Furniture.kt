package com.ec.ardesignkitkat.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Furniture(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "idUser") val idUser: String,
    @ColumnInfo(name = "width") val width: String,
    @ColumnInfo(name = "height") val height: String,
    @ColumnInfo(name = "length") val length: String,
    val nomObjet : String,
    val descObjet: String
) {
    //constructor(id: String, descObjet: String) : this(id = id, descObjet = descObjet)
}