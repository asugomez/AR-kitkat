package com.ec.ardesignkitkat.data.source.remote

import android.util.Log
import com.ec.ardesignkitkat.data.model.Furniture
import com.ec.ardesignkitkat.data.model.StandardFurniture
import com.ec.ardesignkitkat.data.model.User
import com.ec.ardesignkitkat.data.model.Wall
import com.ec.ardesignkitkat.data.source.remote.api.ARDesignAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataProvider {

    private val BASE_URL = "http://192.168.43.156/~asugomez/AR-design/api/"

    var TAG = "ARDesign remotedataprovider"

    val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
    val client: OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
    }.retryOnConnectionFailure(true)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val service = retrofit.create(ARDesignAPI::class.java)

    //////////////      USER        //////////////
    suspend fun connexion(pseudo: String, pass:String): User {
        return service.connexion(pseudo, pass).user
    }

    suspend fun getUsers(hash: String): List<User>{
        return service.getUsers(hash).users
    }

    suspend fun getUserId(hash: String, pseudo: String) : Int {
        return getUsers(hash).first { it.pseudo == pseudo }.id
    }

    suspend fun getUserData(id_user: Int, hash: String): User{
        return service.getUserData(id_user, hash)
    }

    suspend fun mkUser(pseudo: String, pass: String, mail: String){
        return service.mkUser(pseudo, pass, mail)
    }

    //////////////      WALL       //////////////

    suspend fun getWalls(hash: String): List<Wall>{
        return service.getWalls(hash).walls
    }

    suspend fun getWallData(id: Int, hash: String): Wall{
        return service.getWallData(id, hash)
    }

    suspend fun getUsersWalls(id_user: Int, hash: String): List<Wall>{
        return service.getUsersWalls(id_user, hash).walls
    }

    suspend fun getUsersWall(id_user: Int, id_wall: Int, hash: String): Wall{
        return service.getUsersWall(id_user, id_wall, hash)
    }

    suspend fun addUsersWall(id_user: Int, width: String, height: String, hash: String){
        return service.addUsersWall(id_user, width, height, hash)
    }

    suspend fun rmUsersWall(id_user: Int, id_wall: Int, hash: String){
        return service.rmUsersWall(id_user, id_wall, hash)
    }

    //////////////     FURNITURE       //////////////

    suspend fun getFurnitures(hash: String): List<Furniture>{
        return service.getFurnitures(hash).furnitures
    }

    suspend fun getFurnitureData(id: Int, hash: String): Furniture{
        return service.getFurnitureData(id, hash)
    }

    suspend fun getUsersFurnitures(id_user: Int, hash: String): List<Furniture>{
        //Log.v(TAG,"inside remotedata getusers furnitures")
        //Log.v(TAG,service.getUsersFurnitures(id_user, hash).furnitures.toString())
        return service.getUsersFurnitures(id_user, hash).furnitures
    }

    suspend fun getUsersFurniture(id_user: Int, id_furn: Int, hash: String): Furniture{
        return service.getUsersFurniture(id_user, id_furn, hash)
    }

    suspend fun addUsersFurniture(id_user: Int, width: String, height: String, length: String, nom: String,hash: String){
        return service.addUsersFurniture(id_user, width, height, length, nom, hash)
    }

    suspend fun rmUsersFurniture(id_user: Int, id_furn: Int, hash: String){
        return service.rmUsersFurniture(id_user, id_furn, hash)
    }

    //////////////      STAND FURNITURE        //////////////

    suspend fun getStandFurnitures(hash: String): List<StandardFurniture>{
        return service.getStandFurnitures(hash).standFurnitures
    }

    suspend fun getStandFurnitureData(id: Int, hash: String): StandardFurniture{
        return service.getStandFurnitureData(id, hash)
    }

    suspend fun addStandFurniture(width: String, height: String, length: String, url: String, hash: String){
        return service.addStandFurniture(width, height, length, url, hash)
    }



    /*private fun List<User>.toUsers() = this.map { userResponse ->
        User(
            id = userResponse.id,
            pseudo = userResponse.pseudo
            mail = userResponse.mail,
            password = userResponse.password,
            hash = userResponse.hash
        )
    }

    suspend fun getObjectsFurniture(): List<Wall>
        = service.getObjectsFurniture().objects.toObjectsFurniture()

    suspend fun getObjectsFromUser(id: String, hash:String): List<Wall>
            = service.getObjectsFurnitureFromUser(id,hash).objects.toObjectsFurniture()

    private fun List<Wall>.toObjectsFurniture() = this.map { objectFurniture ->
        Wall(
            id = objectFurniture.id,
            id_user = objectFurniture.id_user,
            type = objectFurniture.type,
            width = objectFurniture.width,
            height = objectFurniture.height
        )
    }

     */

}