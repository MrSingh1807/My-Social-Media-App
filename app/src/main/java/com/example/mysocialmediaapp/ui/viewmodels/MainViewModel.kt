package com.example.mysocialmediaapp.ui.viewmodels

import android.app.Application
import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor (
    private val context: Application
        ) : ViewModel() {

     fun validateEmail(email: String): Boolean {

        if (email.isEmpty()){
            Toast.makeText(context, "Please Enter an Email ID", Toast.LENGTH_SHORT).show()

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            val text = "Please Provide a valid Email ID"
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        } else{
            return true
        }
        return false
    }
     fun validatePassword(passWord: String ): Boolean{

        if (passWord.isEmpty()){
            Toast.makeText(context, "Please Enter password", Toast.LENGTH_SHORT).show()

        } else if ( passWord.length < 6){
            val text = "Password does't less than 6"
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        return false
    }
}