package com.ec.ardesignkitkat.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Furniture(
    @PrimaryKey val id: String,
    val idUser: String,
    val width: String,
    val height: String,
    val length: String,
    val nom : String
) {
    //constructor(id: String, descObjet: String) : this(id = id, descObjet = descObjet)
}