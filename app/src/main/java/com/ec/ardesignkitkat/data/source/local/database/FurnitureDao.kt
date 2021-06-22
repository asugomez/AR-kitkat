package com.ec.ardesignkitkat.data.source.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ec.ardesignkitkat.data.model.Furniture
import com.ec.ardesignkitkat.data.model.User

@Dao
interface FurnitureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdate(furnitures: List<Furniture>)

    @Query("SELECT * " +
            "FROM furniture")
    suspend fun getFurnitures(): List<Furniture>

    @Query("SELECT * FROM furniture " +
            "WHERE id=:idFurn AND idUser=:idUser")
    suspend fun getFurnitureData(idFurn: Int, idUser: Int): Furniture

    @Query("SELECT * FROM furniture" +
            " WHERE idUser=:idUser")
    suspend fun getUsersFurnitures(idUser: Int): List<Furniture>

    @Query("SELECT * FROM furniture" +
            " WHERE id=:idFurn AND idUser=:idUser")
    suspend fun getUsersFurniture(idFurn: Int, idUser: Int): Furniture

    @Query("INSERT INTO furniture(idUser, width, height, length, nom) " +
            "VALUES(:idUser, :width, :height, :length, :nomObjet)")
    suspend fun addUsersFurniture(idUser: Int, width: String, height: String, length: String, nomObjet: String)

    @Query("DELETE FROM furniture " +
            "WHERE id=:idFurn AND idUser=:idUser")
    suspend fun rmUsersFurniture(idFurn: Int, idUser: Int)
}