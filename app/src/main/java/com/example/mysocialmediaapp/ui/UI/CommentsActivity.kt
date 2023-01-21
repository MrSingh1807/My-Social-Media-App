package com.example.mysocialmediaapp.ui.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.ActivityCommentsBinding

class CommentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}