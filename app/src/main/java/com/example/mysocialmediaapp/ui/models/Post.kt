package com.example.mysocialmediaapp.ui.models


data class Post(
    var postID: String? = "",
    val postImage: String? = "",
    val postedBy: String? = "",
    val postDescription: String? =  "",
    val postedAt: Long? = 0,
    val postLikes :Int? = 0
)
