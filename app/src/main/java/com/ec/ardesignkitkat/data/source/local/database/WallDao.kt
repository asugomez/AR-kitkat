package com.ec.ardesignkitkat.data.source.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ec.ardesignkitkat.data.model.Wall

@Dao
interface WallDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdate(walls: List<Wall>)


    @Query("SELECT * " +
            "FROM wall")
    suspend fun getWalls(): List<Wall>

    @Query("SELECT * " +
            "FROM wall WHERE id=:idWall")
    suspend fun getWallData(idWall: Int): Wall

    @Query("SELECT * FROM wall " +
            "WHERE idUser=:idUser")
    suspend fun getUsersWalls(idUser: Int): List<Wall>

    @Query("SELECT * FROM wall " +
            "WHERE id=:idWall AND idUser=:idUser")
    suspend fun getUsersWall(idUser: Int, idWall: Int): Wall

    @Query("INSERT INTO wall(idUser, width, height) " +
            "VALUES(:idUser, :width, :height)")
    suspend fun addUsersWall(idUser: Int, width: String, height: String)

    @Query("DELETE FROM wall " +
            "WHERE id=:idWall AND idUser=:idUser")
    suspend fun rmUsersWall(idWall: Int, idUser: Int)
}
