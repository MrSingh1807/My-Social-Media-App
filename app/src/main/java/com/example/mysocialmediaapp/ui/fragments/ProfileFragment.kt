package com.example.mysocialmediaapp.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.FragmentProfileBinding
import com.example.mysocialmediaapp.ui.adapters.MyFriendProfileAdapter
import com.example.mysocialmediaapp.ui.models.MyFriendsProfileImageModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var myFriendsList: ArrayList<MyFriendsProfileImageModel> = ArrayList()

    private lateinit var galleryCoverPicker:  ActivityResultLauncher<Intent>
    private lateinit var galleryProfilePicker:  ActivityResultLauncher<Intent>
    private lateinit var cameraCoverPicker:  ActivityResultLauncher<Intent>
    private lateinit var cameraProfilePicker:  ActivityResultLauncher<Intent>




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        galleryProfilePicker = pickImageFromGallery(binding.profileImgVw)
        cameraProfilePicker = pickImageFromCamera(binding.profileImgVw)

        galleryCoverPicker = pickImageFromGallery(binding.coverProfileIV)
        cameraCoverPicker = pickImageFromCamera(binding.coverProfileIV)


        binding.changeCoverProfileView.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Upload Image")
                .setMessage("Update Your Cover Photo")
                .setPositiveButton("Camera"){ dialogInterface, i ->
                    val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraCoverPicker.launch(intentCamera)
                }
                .setNegativeButton("Gallery"){ dialogInterface, i ->
                    val intentGallery = Intent(Intent.ACTION_PICK)
                    intentGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    galleryCoverPicker.launch(intentGallery)
                }
            dialog.show()
        }
        binding.changeProfilePicView.setOnClickListener {
            val alert = AlertDialog.Builder(requireContext())
                .setTitle("Upload Image")
                .setMessage("Update Your Cover Photo")
                .setPositiveButton("Camera"){ dialogInterface, i ->
                    val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    galleryProfilePicker.launch(intentCamera)
                }
                .setNegativeButton("Gallery"){ dialogInterface, i ->
                    val intentGallery = Intent(Intent.ACTION_PICK)
                    intentGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    galleryProfilePicker.launch(intentGallery)
                }
                alert.show()
        }

        setFriendsListRecyclerView()

        return binding.root
    }


    fun pickImageFromGallery (imageView: ImageView) = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val galleryUri = result.data!!.data
                   imageView.setImageURI(galleryUri)
                }
            }
    fun pickImageFromCamera(imageView: ImageView) = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data != null) {
                val cameraBitmap = it.data?.extras?.get("data") as Bitmap
                imageView.setImageBitmap(cameraBitmap)
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