package com.example.mysocialmediaapp.ui.fragments.signinorloginfragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mysocialmediaapp.databinding.FragmentLogInBinding
import com.example.mysocialmediaapp.ui.UI.MainActivity
import com.example.mysocialmediaapp.ui.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLogInBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInBTN.setOnClickListener {
            val email = binding.signUpEmailET.text.toString().trim()
            val password = binding.singUpPasswordET.text.toString().trim()

            if (mainViewModel.validateEmail(email) || mainViewModel.validatePassword(password)) {
                mainViewModel.firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Toast.makeText(context, "User Signed In", Toast.LENGTH_SHORT).show()

                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            "User Id & Password Not Matched",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

        binding.forSignInTV.setOnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}