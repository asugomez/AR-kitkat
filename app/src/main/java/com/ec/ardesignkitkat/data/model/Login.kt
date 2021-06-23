package com.ec.ardesignkitkat.data.model

import com.google.gson.annotations.SerializedName


data class Login(
    // user, mdp, lists of the user
    @SerializedName("version")
    val version: String,
    @SerializedName("success")
    val succes: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("user")
    val user: User
)