package com.jonatan.temantani.model

//data class Data (
//    val id : String? = null,
//    val name : String? = null,
//    val email : String? = null,
//    val address : String? = null,
//)

data class LoginResponse(
    val message: String,
    val data: UserData,
    val status: Int
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String,
    val address: String
)
