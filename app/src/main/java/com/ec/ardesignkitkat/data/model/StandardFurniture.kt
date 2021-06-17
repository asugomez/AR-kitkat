package com.ec.ardesignkitkat.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StandardFurniture(
    @PrimaryKey val id: Int,
    val width: String,
    val height: String,
    val length: String,
    val url: String
)