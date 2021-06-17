package com.ec.ardesignkitkat.data

import android.app.Application
import com.ec.ardesignkitkat.data.model.User
import com.ec.ardesignkitkat.data.model.Wall
import com.ec.ardesignkitkat.data.source.local.LocalDataProvider
import com.ec.ardesignkitkat.data.source.remote.RemoteDataProvider

class WallRepository(
    private val localDataProvider: LocalDataProvider,
    private val remoteDataProvider: RemoteDataProvider
) {
    companion object {
        fun newInstance(application: Application): WallRepository {
            return WallRepository(
                localDataProvider = LocalDataProvider(application),
                remoteDataProvider = RemoteDataProvider()
            )
        }
    }

    suspend fun getWalls(hash: String): List<Wall>{
        return try{
            remoteDataProvider.getWalls(hash).also {
                localDataProvider.saveOrUpdateWall(it)
            }
        } catch (e:Exception){
            localDataProvider.getWalls()
        }
    }

    suspend fun getWallData(idWall: Int, hash: String): Wall{
        return try{
            remoteDataProvider.getWallData(idWall, hash).also {
                localDataProvider.saveOrUpdateWall(listOf(it))
            }
        } catch (e:Exception){
            localDataProvider.getWallData(idWall)
        }
    }

    suspend fun getUsersWalls(id_user: Int, hash: String): List<Wall>{
        return try{
            remoteDataProvider.getUsersWalls(id_user, hash).also {
                localDataProvider.saveOrUpdateWall(it)
            }
        } catch (e:Exception){
            localDataProvider.getUsersWalls(id_user)
        }
    }

    suspend fun getUsersWall(id_user: Int, id_wall: Int, hash: String): Wall{
        return try{
            remoteDataProvider.getUsersWall(id_user, id_wall, hash).also {
                localDataProvider.saveOrUpdateWall(listOf(it))
            }
        } catch (e:Exception){
            localDataProvider.getUsersWall(id_user, id_wall)
        }
    }

    suspend fun addUsersWall(id_user: Int, width: String, height: String, hash:String){
        return try{
            remoteDataProvider.addUsersWall(id_user, width, height, hash)
        } catch (e:Exception){
            localDataProvider.addUsersWall(id_user, width, height)
        }
    }

    suspend fun rmUsersWall(idUser: Int, idWall: Int,  hash:String){
        return try{
            remoteDataProvider.rmUsersWall(idUser, idWall, hash)
        } catch (e:Exception){
            localDataProvider.rmUsersWall(idWall, idUser)
        }
    }



}