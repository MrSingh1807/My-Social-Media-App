package com.example.mysocialmediaapp.ui.models

data class NotificationModel(

    var notificationId: String? = "",
    val notificationBy: String? = "",
    val notificationAt: Long? = 0,
    val type: String? = "",
    val postID: String? = "",
    val postedBy: String? = "",
    val checkOpen: Boolean? = false

)
