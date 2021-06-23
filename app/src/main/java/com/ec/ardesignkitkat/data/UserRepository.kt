package com.ec.ardesignkitkat.data

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.ec.ardesignkitkat.data.model.User
import com.ec.ardesignkitkat.data.source.local.LocalDataProvider
import com.ec.ardesignkitkat.data.source.remote.RemoteDataProvider

class UserRepository(
    private val localDataProvider: LocalDataProvider,
    private val remoteDataProvider: RemoteDataProvider
) {

    public val TAG: String = "ARDesign"

    companion object {
        fun newInstance(application: Application): UserRepository {
            return UserRepository(
                localDataProvider = LocalDataProvider(application),
                remoteDataProvider = RemoteDataProvider()
            )
        }
    }

    suspend fun connexion(pseudo: String, pass:String): User {
        return try{
            Log.v(TAG, "function connexion" )
            Log.v(TAG, "pseudo: " +pseudo )
            Log.v(TAG, "passsword. " + pass )
            remoteDataProvider.connexion(pseudo,pass).also {
                Log.v(TAG, "here in also connexion" )
                localDataProvider.saveOrUpdateUser(listOf(it)) // dont know if it works
            }
        } catch (e: Exception){
            Log.v(TAG, "function connexion EXCEPTion" )
            localDataProvider.connexion(pseudo, pass)
        }
    }

    suspend fun getUsers(hash: String): List<User>{
        return try{
            remoteDataProvider.getUsers(hash).also {
                localDataProvider.saveOrUpdateUser(it)
            }
        } catch (e:Exception){
            localDataProvider.getUsers()
        }
    }

    suspend fun getUserData(id_user: Int, hash: String): User{
        return try{
            remoteDataProvider.getUserData(id_user, hash).also {
                localDataProvider.saveOrUpdateUser(listOf(it))
            }
        } catch (e: Exception){
            localDataProvider.getUserData(id_user)
        }
    }

    suspend fun mkUser(pseudo: String, pass: String, mail: String): Boolean{

        Log.v(TAG, "Creation user" )
        val user = remoteDataProvider.mkUser(pseudo, pass, mail)
        if(user!=null){
            return true
        }
        return false
    }
}