package com.example.mysocialmediaapp.ui.models

data class User(
    val name: String? = "",
    val profession: String? = "",
    val email:String? = "",
    val password: String? = "",
    val coverPhoto: String? = "",
    val profilePhoto : String? = ""
)
//{
//    constructor(name: String, profession: String, email: String, password: String): this(name, profession, email, password, "" )
//}
