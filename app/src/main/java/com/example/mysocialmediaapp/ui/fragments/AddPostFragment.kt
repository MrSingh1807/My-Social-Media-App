package com.example.mysocialmediaapp.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.FragmentAddPostBinding
import com.example.mysocialmediaapp.ui.models.Post
import com.example.mysocialmediaapp.ui.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*

class AddPostFragment : Fragment() {

    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var uri: Uri
//    lateinit var dialog: ProgressBar

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseStorage: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
//        dialog = ProgressBar(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        // Fetch Data from Firebase DataBase
        firebaseDatabase.reference.child("Users")
            .child(firebaseAuth.uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        Picasso.get().load(user?.profilePhoto)
                            .placeholder(R.drawable.cute_dog)
                            .into(binding.profileImgVw)

                        binding.userNameTV.text = user?.name
                        binding.userProfessionTV.text = user?.profession
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        //Edit Texts
        binding.postDescriptionET.doOnTextChanged { text, start, before, count ->
            if (text!!.isNotEmpty()) {
                showPostBTN(R.drawable.follow_btn_bg, R.color.white, true)
            } else {
                showPostBTN(R.drawable.follow_active_btn, R.color.mediumGray, false)
            }
        }

        //ImageViews
        binding.uploadPostImageIV.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Upload Image")
                .setMessage("Update Your Cover Photo")
                .setPositiveButton("Camera") { dialogInterface, i ->
                    val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    pickCoverImageFromCamera.launch(intentCamera)
                }
                .setNegativeButton("Gallery") { dialogInterface, i ->
                    val intentGallery = Intent(Intent.ACTION_PICK)
                    intentGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    pickCoverImageFromGallery.launch(intentGallery)
                }
                .setNeutralButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
            dialog.show()
        }

        // Buttons
        binding.postBTN.setOnClickListener {
            val refrence = firebaseStorage.reference.child("posts")
                .child(firebaseAuth.uid!!)
                .child(Date().time.toString())
            refrence.putFile(uri).addOnSuccessListener {
                refrence.downloadUrl.addOnSuccessListener {
                    val post = Post(
                        postImage = uri.toString(),
                        postedBy = firebaseAuth.uid!!,
                        postDescription = binding.postDescriptionET.text.toString(),
                        postedAt = Date().time,
                    )
                    firebaseDatabase.reference.child("posts")
                        .push()
                        .setValue(post).addOnSuccessListener {
                            Toast.makeText(context, "Posted Successful", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

    }


    private val pickCoverImageFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uri = result.data?.data!!
                binding.previewUploadedPostImageIV.visibility = View.VISIBLE
                binding.previewUploadedPostImageIV.setImageURI(uri)

                showPostBTN(R.drawable.follow_btn_bg, R.color.white, true)
            }
        }
    private val pickCoverImageFromCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null) {
                    val cameraBitmap = it.data?.extras?.get("data") as Bitmap

                    binding.previewUploadedPostImageIV.visibility = View.VISIBLE
                    binding.previewUploadedPostImageIV.setImageBitmap(cameraBitmap)
                }
            }
        }

    private fun showPostBTN(drawable: Int, color: Int, isEnabled: Boolean) {
        binding.postBTN.setBackgroundDrawable(context?.let {
            ContextCompat.getDrawable(
                it, drawable
            )
        })
        context?.resources?.getColor(color)
            ?.let { binding.postBTN.setTextColor(it) }
        binding.postBTN.isEnabled = isEnabled
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}