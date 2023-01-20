package com.example.mysocialmediaapp.ui.models


data class Post(
    val postID: String,
    val postImage: String,
    val postedBy: String,
    val postDescription: String,
    val postedAt: Long
) {
    constructor( postImage: String,
                 postedBy: String,
                 postDescription: String,
                 postedAt: Long) :this ( "", postImage, postedBy, postDescription, postedAt)
}
