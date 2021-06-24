package com.ec.ardesignkitkat.data.source.remote.api

import com.ec.ardesignkitkat.data.model.*
import retrofit2.http.*

interface ARDesignAPI {

    //////////////      USER        //////////////
    @POST("authenticate")
    suspend fun connexion(@Query("user") user: String,
                          @Query("password") password: String) : Login

    @GET("users")
    suspend fun getUsers(@Header("hash") hash: String): UserResponse

    @GET("users/{id}")
    suspend fun getUserData(@Path("id") id: Int,
                            @Header("hash") hash: String): User

    @POST("users")
    suspend fun mkUser(@Query("pseudo") pseudo: String,
                       @Query("password") pass: String,
                       @Query("mail") mail: String): Login



    //////////////      WALL       //////////////
    @GET("walls")
    suspend fun getWalls(@Header("hash") hash: String): WallResponse

    @GET("walls/{id}")
    suspend fun getWallData(@Path("id") id: Int,
                            @Header("hash") hash: String): Wall

    @GET("users/{idUser}/walls")
    suspend fun getUsersWalls(@Path("idUser") id_user: Int,
                              @Header("hash") hash: String): WallResponse

    @GET("users/{idUser}/walls/{idWall}")
    suspend fun getUsersWall(@Path("idUser") id_user: Int,
                              @Path("idWall") id_wall: Int,
                              @Header("hash") hash: String): Wall


    @POST("users/{idUser}/walls")
    suspend fun addUsersWall(@Path("idUser") id_user: Int,
                             @Query("width") width: String,
                             @Query("heigth") heigth: String,
                             @Header("hash") hash: String)

    @DELETE("users/{idUser}/wallss/{idWall}")
    suspend fun rmUsersWall(@Path("idUser") id_user: Int,
                            @Path("idWall") id_wall: Int,
                            @Header("hash") hash: String)

    //////////////     FURNITURE       //////////////
    @GET("furnitures")
    suspend fun getFurnitures(@Header("hash") hash: String): FurnitureResponse

    @GET("furnitures/{id}")
    suspend fun getFurnitureData(@Path("id") id: Int,
                                @Header("hash") hash: String): Furniture

    @GET("users/{idUser}/furnitures")
    suspend fun getUsersFurnitures(@Path("idUser") id_user: Int,
                                   @Header("hash") hash: String): FurnitureResponse

    @GET("users/{idUser}/furnitures/{idFurn}")
    suspend fun getUsersFurniture(@Path("idUser") id_user: Int,
                                   @Path("idFurn") id_furn: Int,
                                   @Header("hash") hash: String): Furniture

    @POST("users/{idUser}/furnitures")
    suspend fun addUsersFurniture(@Path("idUser") id_user: Int,
                                  @Query("width") width: String,
                                  @Query("height") heigth: String,
                                  @Query("length") length: String,
                                  @Query("nom") nom : String,
                                  @Header("hash") hash: String)

    @DELETE("users/{idUser}/furnitures/{idFurn}")
    suspend fun rmUsersFurniture(@Path("idUser") id_user: Int,
                                 @Path("idFurn") id_furn: Int,
                                 @Header("hash") hash: String)


    //////////////      STAND FURNITURE        //////////////
    @GET("standardFurnitures")
    suspend fun getStandFurnitures(@Header("hash") hash: String): StandardFurnitureResponse

    @GET("standardFurnitures/{id}")
    suspend fun getStandFurnitureData(@Path("id") id: Int,
                                      @Header("hash") hash: String): StandardFurniture

    @POST("standardFurnitures")
    suspend fun addStandFurniture(@Query("width") width: String,
                                  @Query("heigth") heigth: String,
                                  @Query("length") length: String,
                                  @Query("url") url: String,
                                  @Header("hash") hash: String)

    /*@DELETE("users/{idUser}/standardFurnitures/{idStanFurn}")
    suspend fun rmStandFurniture(@Path("idUser") id_user: Int,
                                 @Path("idStanFurn") id_standFurn: Int,
                                 @Header("hash") hash: String)*/
}