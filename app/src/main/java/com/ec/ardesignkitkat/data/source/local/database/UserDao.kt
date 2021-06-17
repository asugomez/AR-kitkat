package com.ec.ardesignkitkat.data.source.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ec.ardesignkitkat.data.model.User
import com.ec.ardesignkitkat.data.model.Wall


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdate(users: List<User>)

    @Query("SELECT * FROM user WHERE " +
            "pseudo=:pseudo AND pass=:pass")
    suspend fun connexion(pseudo: String, pass:String): User

    @Query("SELECT * " +
            "FROM user")
    suspend fun getUsers(): List<User>

    @Query("SELECT * FROM user" +
            " WHERE id=:idUser")
    suspend fun getUserData(idUser: Int): User

    @Query("INSERT INTO user(pseudo,mail,pass) " +
            "VALUES(:pseudo, :mail, :pass)")
    suspend fun mkUser(pseudo: String, mail: String, pass: String)//: User
}