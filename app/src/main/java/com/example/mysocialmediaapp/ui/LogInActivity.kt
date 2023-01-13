package com.example.mysocialmediaapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.ActivityLogInBinding
import com.example.mysocialmediaapp.databinding.FragmentSignUpBinding

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}