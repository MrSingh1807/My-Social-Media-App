package com.example.mysocialmediaapp.ui.viewmodels

import android.app.Application
import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val firebaseStorage = FirebaseStorage.getInstance()

    //    Firebase Realtime DataBase
    val uid = firebaseAuth.uid
    val currentUserUID = firebaseAuth.currentUser?.uid


    //    Firebase Realtime DataBase
    val notificationFirebaseDB = firebaseDatabase.reference.child("Notification")
    val userFirebaseDB = firebaseDatabase.reference.child("Users")
    val postFirebaseDB = firebaseDatabase.reference.child("posts")
    val storiesFirebaseDB = firebaseDatabase.reference.child("stories")


    //    Firebase Storage
    val coverPhotoFirebaseStorage = firebaseStorage.reference.child("cover_photo")
    val postsFirebaseStorage = firebaseStorage.reference.child("posts")
    val profilePhotoFirebaseStorage = firebaseStorage.reference.child("profile_photo")
    val storiesFirebaseStorage = firebaseStorage.reference.child("stories")

    fun validateEmail(email: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(context, "Please Enter an Email ID", Toast.LENGTH_SHORT).show()

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            val text = "Please Provide a valid Email ID"
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        return false
    }

    fun validatePassword(passWord: String): Boolean {

        if (passWord.isEmpty()) {
            Toast.makeText(context, "Please Enter password", Toast.LENGTH_SHORT).show()

        } else if (passWord.length < 6) {
            val text = "Password does't less than 6"
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        return false
    }
}