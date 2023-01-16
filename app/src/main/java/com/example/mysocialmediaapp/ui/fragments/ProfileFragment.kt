package com.example.mysocialmediaapp.ui.fragments

import android.R.attr.bitmap
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.FragmentProfileBinding
import com.example.mysocialmediaapp.ui.adapters.MyFriendProfileAdapter
import com.example.mysocialmediaapp.ui.models.MyFriendsProfileImageModel
import com.example.mysocialmediaapp.ui.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var myFriendsList: ArrayList<MyFriendsProfileImageModel> = ArrayList()

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var firebaseDatabase: FirebaseDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        binding.changeCoverProfileView.setOnClickListener {
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
            dialog.show()
        }
        binding.changeProfilePicView.setOnClickListener {
            val alert = AlertDialog.Builder(requireContext())
                .setTitle("Upload Image")
                .setMessage("Update Your Cover Photo")
                .setPositiveButton("Camera") { dialogInterface, i ->
                    val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    pickProfileImageFromCamera.launch(intentCamera)
                }
                .setNegativeButton("Gallery") { dialogInterface, i ->
                    val intentGallery = Intent(Intent.ACTION_PICK)
                    intentGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    pickProfileImageFromGallery.launch(intentGallery)
                }
            alert.show()
        }

        firebaseDatabase.reference.child("Users").child(firebaseAuth.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        Picasso.get().load(user?.coverPhoto).placeholder(R.drawable.ic_image_search)
                            .into(binding.coverProfileIV)
                        Picasso.get().load(user?.profilePhoto).placeholder(R.drawable.ic_image_search)
                            .into(binding.profileImgVw)

                        binding.userNameTV.text = user!!.name
                        binding.professionTV.text = user.profession
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        setFriendsListRecyclerView()

        return binding.root
    }


    private val pickCoverImageFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val galleryUri = result.data?.data!!
            binding.coverProfileIV.setImageURI(galleryUri)

            val uid = FirebaseAuth.getInstance().uid!!
            val refrence = firebaseStorage.reference.child("cover_photo").child(uid)
            refrence.putFile(galleryUri).addOnSuccessListener {
                Toast.makeText(requireContext(), "Cover Pic Saved", Toast.LENGTH_SHORT).show()

                refrence.downloadUrl.addOnSuccessListener {
                    firebaseDatabase.reference.child("Users").child(uid).child("coverPhoto")
                        .setValue(it.toString())
                }
            }
        }
    }
    private val pickCoverImageFromCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null) {
                    val cameraBitmap = it.data?.extras?.get("data") as Bitmap
                    binding.coverProfileIV.setImageBitmap(cameraBitmap)

                    val stream = ByteArrayOutputStream()
                    cameraBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val cameraBites = stream.toByteArray()
                    cameraBitmap.recycle()


                    val uid = FirebaseAuth.getInstance().uid!!
                    val refrence = firebaseStorage.reference.child("cover_photo").child(uid)
                    refrence.putBytes(cameraBites).addOnSuccessListener {
                        Toast.makeText(requireContext(), "Cover Pic Saved", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    private val pickProfileImageFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val galleryUri = result.data?.data!!
                binding.profileImgVw.setImageURI(galleryUri)

                val uid = FirebaseAuth.getInstance().uid!!
                val refrence = firebaseStorage.reference.child("profile_photo").child(uid)
                refrence.putFile(galleryUri).addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile Pic Saved", Toast.LENGTH_SHORT).show()

                    refrence.downloadUrl.addOnSuccessListener {
                        firebaseDatabase.reference.child("Users").child(uid).child("profilePhoto")
                            .setValue(it.toString())
                    }
                }
            }
        }
    private val pickProfileImageFromCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null) {
                    val cameraBitmap = it.data?.extras?.get("data") as Bitmap
                    binding.profileImgVw.setImageBitmap(cameraBitmap)

                    val stream = ByteArrayOutputStream()
                    cameraBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val cameraBites = stream.toByteArray()
                    cameraBitmap.recycle()

                    val uid = FirebaseAuth.getInstance().uid!!
                    val refrence = firebaseStorage.reference.child("profile_photo").child(uid)
                    refrence.putBytes(cameraBites).addOnSuccessListener {
                        Toast.makeText(requireContext(), "Profile Pic Saved", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }


    private fun setFriendsListRecyclerView() {
        myFriendsList.add(MyFriendsProfileImageModel(R.drawable.cute_dog))
        myFriendsList.add(MyFriendsProfileImageModel(R.drawable.cute_dog))
        myFriendsList.add(MyFriendsProfileImageModel(R.drawable.cute_dog))
        myFriendsList.add(MyFriendsProfileImageModel(R.drawable.cute_dog))
        myFriendsList.add(MyFriendsProfileImageModel(R.drawable.cute_dog))
        myFriendsList.add(MyFriendsProfileImageModel(R.drawable.cute_dog))
        myFriendsList.add(MyFriendsProfileImageModel(R.drawable.cute_dog))

        val friendAdapter = MyFriendProfileAdapter(requireContext(), myFriendsList)
        binding.friendsProfileRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.friendsProfileRV.isNestedScrollingEnabled = false
        binding.friendsProfileRV.adapter = friendAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}