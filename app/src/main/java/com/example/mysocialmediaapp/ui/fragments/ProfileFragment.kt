package com.example.mysocialmediaapp.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.FragmentProfileBinding
import com.example.mysocialmediaapp.ui.UI.LogInActivity
import com.example.mysocialmediaapp.ui.adapters.FollowerAdapter
import com.example.mysocialmediaapp.ui.models.FollowModel
import com.example.mysocialmediaapp.ui.models.User
import com.example.mysocialmediaapp.ui.viewmodels.MainViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var myFriendsList: ArrayList<FollowModel> = ArrayList()

    private val mainViewModel by viewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.log_out, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                mainViewModel.firebaseAuth.signOut()
                return true
            }
        },viewLifecycleOwner, Lifecycle.State.RESUMED)

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
                .setNeutralButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
            dialog.show()
        }
        binding.changeProfilePicView.setOnClickListener {
            val alert = AlertDialog.Builder(requireContext())
                .setTitle("Upload Image")
                .setMessage("Update Your Cover Photo")
                .setPositiveButton("Camera") { dialogInterface, int ->
                    val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    pickProfileImageFromCamera.launch(intentCamera)
                }
                .setNegativeButton("Gallery") { dialogInterface, i ->
                    val intentGallery = Intent(Intent.ACTION_PICK)
                    intentGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    pickProfileImageFromGallery.launch(intentGallery)
                }
                .setNeutralButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
            alert.show()
        }

        //Fetch User Data from database
        mainViewModel.userFirebaseDB.child(mainViewModel.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)!!
                        if (user.coverPhoto.isNullOrEmpty()){
                            binding.coverProfileIV.setImageResource(R.drawable.kid)
                        }else {
                            Picasso.get().load(user.coverPhoto).placeholder(R.drawable.ic_image_search)
                                .into(binding.coverProfileIV)
                        }

                        if (user.profilePhoto.isNullOrEmpty()){
                            binding.profileImgVw.setImageResource(R.drawable.cute_dog)
                        } else {
                            Picasso.get().load(user.profilePhoto)
                                .placeholder(R.drawable.ic_image_search)
                                .into(binding.profileImgVw)
                        }

                        binding.userNameTV.text = user.name
                        binding.professionTV.text = user.profession
                        binding.followerCountTV.text = user.followerCount.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        setFriendsListRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.log_out, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return if (menuItem.itemId == R.id.logOut) {
                    mainViewModel.firebaseAuth.signOut()

                    startActivity(Intent(requireActivity(), LogInActivity::class.java))
                    true
                } else {
                    false
                }
            }
        })
    }

    private val pickCoverImageFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val galleryUri = result.data?.data!!
                binding.coverProfileIV.setImageURI(galleryUri)

                val refrence = mainViewModel.coverPhotoFirebaseStorage.child(mainViewModel.uid!!)
                refrence.putFile(galleryUri).addOnSuccessListener {
                    Toast.makeText(requireContext(), "Cover Pic Saved", Toast.LENGTH_SHORT).show()

                    refrence.downloadUrl.addOnSuccessListener {
                        mainViewModel.userFirebaseDB.child(mainViewModel.uid!!).child("coverPhoto")
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


                    val refrence = mainViewModel.coverPhotoFirebaseStorage.child(mainViewModel.uid!!)
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

                val refrence = mainViewModel.profilePhotoFirebaseStorage.child(mainViewModel.uid!!)
                refrence.putFile(galleryUri).addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile Pic Saved", Toast.LENGTH_SHORT).show()

                    refrence.downloadUrl.addOnSuccessListener {
                        mainViewModel.userFirebaseDB.child(mainViewModel.uid!!).child("profilePhoto")
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

                    val refrence = mainViewModel.profilePhotoFirebaseStorage.child(mainViewModel.uid!!)
                    refrence.putBytes(cameraBites).addOnSuccessListener {
                        Toast.makeText(requireContext(), "Profile Pic Saved", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }


    private fun setFriendsListRecyclerView() {

        val friendAdapter = FollowerAdapter(requireContext(), myFriendsList, mainViewModel)
        binding.friendsProfileRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.friendsProfileRV.isNestedScrollingEnabled = false
        binding.friendsProfileRV.adapter = friendAdapter

        //get Data from Firebase
        mainViewModel.userFirebaseDB
            .child(mainViewModel.uid!!)
            .child("follower")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    myFriendsList.clear()

                    for (DataSnapShot in snapshot.children) {
                        val follow = DataSnapShot.getValue(FollowModel::class.java)!!
                        myFriendsList.add(follow)
                    }
                    friendAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}