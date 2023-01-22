package com.example.mysocialmediaapp.ui.models


data class StoryModel (
    val storyBy: String? = "",
    val storyAt: Long? = 0,
    val stories: ArrayList<UserStories>? = ArrayList()
)

data class UserStories (
    val image: String? = "",
    val storyAt: Long? = 0
)