package com.example.mysocialmediaapp.ui.models

data class User(
    val name: String? = "",
    val profession: String? = "",
    var userID: String? = "",
    val followerCount : Int? = 0,

    val email:String? = "",
    val password: String? = "",
    val coverPhoto: String? = "",
    val profilePhoto : String? = ""
)
