package com.ec.ardesignkitkat.data

import android.app.Application
import com.ec.ardesignkitkat.data.model.Furniture
import com.ec.ardesignkitkat.data.model.Wall
import com.ec.ardesignkitkat.data.source.local.LocalDataProvider
import com.ec.ardesignkitkat.data.source.remote.RemoteDataProvider

class FurnitureRepository(
    private val localDataProvider: LocalDataProvider,
    private val remoteDataProvider: RemoteDataProvider
) {
    companion object {
        fun newInstance(application: Application): FurnitureRepository {
            return FurnitureRepository(
                localDataProvider = LocalDataProvider(application),
                remoteDataProvider = RemoteDataProvider()
            )
        }
    }

    suspend fun getFurnitures(hash: String): List<Furniture>{
        return try{
            remoteDataProvider.getFurnitures(hash).also {
                localDataProvider.saveOrUpdateFurnitures(it)
            }
        } catch (e:Exception){
            localDataProvider.getFurnitures()
        }
    }

    suspend fun getFurnitureData(idFurn: Int, idUser: Int, hash: String): Furniture{
        return try{
            remoteDataProvider.getFurnitureData(idFurn, hash).also {
                localDataProvider.saveOrUpdateFurnitures(listOf(it))
            }
        } catch (e:Exception){
            localDataProvider.getFurnitureData(idFurn, idUser)
        }
    }

    suspend fun getUsersFurnitures(idUser: Int, hash: String): List<Furniture>{
        return try{
            remoteDataProvider.getUsersFurnitures(idUser, hash).also {
                localDataProvider.saveOrUpdateFurnitures(it)
            }
        } catch (e:Exception){
            localDataProvider.getUsersFurnitures(idUser)
        }
    }

    suspend fun getUsersFurniture(idUser: Int,  idFurn: Int, hash: String): Furniture{
        return try{
            remoteDataProvider.getUsersFurniture(idUser, idFurn, hash).also {
                localDataProvider.saveOrUpdateFurnitures(listOf(it))
            }
        } catch (e:Exception){
            localDataProvider.getUsersFurniture(idFurn, idUser)
        }
    }

    suspend fun addUsersFurniture(id_user: Int, width: String, height: String, length: String, hash: String){
        return try{
            remoteDataProvider.addUsersFurniture(id_user, width, height, length, hash)
        } catch (e:Exception){
            localDataProvider.addUsersFurniture(id_user, width, height, length)
        }
    }

    suspend fun rmUsersFurniture(id_user: Int, id_furn: Int, hash: String){
        return try{
            remoteDataProvider.rmUsersFurniture(id_user, id_furn, hash)
        } catch (e:Exception){
            localDataProvider.rmUsersFurniture(id_furn, id_user)
        }
    }





}


    /*
    suspend fun getObjectsFurniture(): List<Wall>{
        return try{
            remoteDataProvider.getObjectsFurniture().also {
                localDataProvider.saveOrUpdate(it)
            }
        } catch (e: Exception){
            localDataProvider.getObjectsFurniture()
        }
    }
     */
