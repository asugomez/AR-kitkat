package com.ec.ardesignkitkat.data

import android.app.Application
import com.ec.ardesignkitkat.data.model.Furniture
import com.ec.ardesignkitkat.data.model.StandardFurniture
import com.ec.ardesignkitkat.data.source.local.LocalDataProvider
import com.ec.ardesignkitkat.data.source.remote.RemoteDataProvider

class StandardFurnitureRepository(
    private val localDataProvider: LocalDataProvider,
    private val remoteDataProvider: RemoteDataProvider
) {
    companion object {
        fun newInstance(application: Application): StandardFurnitureRepository {
            return StandardFurnitureRepository(
                localDataProvider = LocalDataProvider(application),
                remoteDataProvider = RemoteDataProvider()
            )
        }
    }
    suspend fun getStandFurnitures(hash: String): List<StandardFurniture>{
        return try{
            remoteDataProvider.getStandFurnitures(hash).also {
                localDataProvider.saveOrUpdateStandFurn(it)
            }
        } catch (e:Exception){
            localDataProvider.getStandFurnitures()
        }
    }


    suspend fun getStandFurnitureData(idFurn: Int, hash: String): StandardFurniture {
        return try{
            remoteDataProvider.getStandFurnitureData(idFurn, hash).also {
                localDataProvider.saveOrUpdateStandFurn(listOf(it))
            }
        } catch (e:Exception){
            localDataProvider.getStandFurnitureData(idFurn)
        }
    }

    suspend fun addUsersFurniture(width: String, height: String, length: String, url: String,hash: String){
        return try{
            remoteDataProvider.addStandFurniture(width, height, length, url, hash)
        } catch (e:Exception){
            localDataProvider.addStandFurniture(width, height, length, url)
        }
    }
}