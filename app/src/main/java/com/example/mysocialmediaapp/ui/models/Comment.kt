package com.example.mysocialmediaapp.ui.models

data class Comment(
    val commentBody: String? = "",
    val commentedAt: Long? = 0,
    val commentedBy : String? = ""
)
