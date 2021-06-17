package com.ec.ardesignkitkat.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: Int,
    val pseudo: String,
    val mail: String,
    val pass: String,
    val hash: String
)