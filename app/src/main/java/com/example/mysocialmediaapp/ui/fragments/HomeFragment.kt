package com.example.mysocialmediaapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialmediaapp.databinding.FragmentHomeBinding
import com.example.mysocialmediaapp.ui.adapters.PostAdapter
import com.example.mysocialmediaapp.ui.adapters.StoryAdapter
import com.example.mysocialmediaapp.ui.models.Post
import com.example.mysocialmediaapp.ui.models.StoryModel
import com.example.mysocialmediaapp.ui.models.UserStories
import com.example.mysocialmediaapp.ui.viewmodels.MainViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var storyList: ArrayList<StoryModel> = ArrayList()
    private var postList: ArrayList<Post> = ArrayList()

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setUpStoryRecyclerView()
        setUpDashBoardRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addStoryIV.setOnClickListener {
            val intentGallery = Intent(Intent.ACTION_PICK)
            intentGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            galleryLauncher.launch(intentGallery)
        }
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val galleryUri = result.data?.data!!
//                binding.postBackgroundIV.setImageURI(galleryUri)

                val refrence = mainViewModel.storiesFirebaseStorage.child(mainViewModel.uid!!)
                    .child(Date().time.toString())
                refrence.putFile(galleryUri).addOnSuccessListener {

                    refrence.downloadUrl.addOnSuccessListener { imageUri ->
                        val story = StoryModel(storyAt = Date().time)
                        mainViewModel.storiesFirebaseDB.child(mainViewModel.uid!!).child("postedBy")
                            .setValue(story).addOnSuccessListener {
                                val userStories = UserStories(imageUri.toString(), story.storyAt)

                                mainViewModel.storiesFirebaseDB.child(mainViewModel.uid!!)
                                    .child("userStories").push()
                                    .setValue(userStories)

                                Toast.makeText(
                                    context,
                                    "Story Posted Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
            }
        }

    private fun setUpStoryRecyclerView() {
        val storyAdapter = StoryAdapter(requireContext(), storyList, mainViewModel)

        binding.storyRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.storyRecyclerView.isNestedScrollingEnabled = false
        binding.storyRecyclerView.adapter = storyAdapter

        mainViewModel.storiesFirebaseDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                storyList.clear()
                if (snapshot.exists()) {
                    for (dataSnapShot in snapshot.children) {
                        val stories = ArrayList<UserStories>()
                        for (snap in dataSnapShot.child("userStories").children) {
                            val userStory = snap.getValue(UserStories::class.java)!!
                            stories.add(userStory)
                        }

                        val story = StoryModel(
                            storyBy = dataSnapShot.key,
//                                storyAt = dataSnapShot.child("postedBy").getValue(Long::class.java),
                            stories = stories
                        )
                        storyList.add(story)
                    }
                    storyAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun setUpDashBoardRecyclerView() {
        showShimmerEffect()
        binding.dashBoardRV.layoutManager = LinearLayoutManager(requireContext())
        binding.storyRecyclerView.isNestedScrollingEnabled = false
        val postAdapter = PostAdapter(requireContext(), postList, mainViewModel)
        binding.dashBoardRV.adapter = postAdapter

        mainViewModel.postFirebaseDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                for (dataSnapshot in snapshot.children) {
                    val post = dataSnapshot.getValue(Post::class.java)!!
                    post.postID = dataSnapshot.key
                    postList.add(post)
                }
                hideShimmerEffect()
                postAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                showShimmerEffect()
            }
        })

    }

    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.dashBoardRV.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.dashBoardRV.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}