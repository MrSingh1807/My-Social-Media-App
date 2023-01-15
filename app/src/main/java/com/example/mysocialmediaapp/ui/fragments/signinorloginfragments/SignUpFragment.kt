package com.example.mysocialmediaapp.ui.fragments.signinorloginfragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mysocialmediaapp.databinding.FragmentSignUpBinding
import com.example.mysocialmediaapp.ui.models.User
import com.example.mysocialmediaapp.ui.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)



        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.forSignInTV.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.signUpBTN.setOnClickListener {

            val userName = binding.signUpUserNameET.text.toString().trim()
            val profession = binding.signUpUserProfessionET.text.toString().trim()
            val email = binding.signUpEmailET.text.toString().trim()
            val password = binding.singUpPasswordET.text.toString().trim()

            if (mainViewModel.validateEmail(email) || mainViewModel.validatePassword(password)) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        if (userName.isNotEmpty() || profession.isNotEmpty()) {
                            val user = User(userName, profession, email, password)
                            val id = it.user!!.uid
                            database.getReference("Users").child(id).setValue(user)
                        }
                        Toast.makeText(context, "User Created", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Authentication Failed", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("SignUp", it.message.toString())
                    }
            }
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}