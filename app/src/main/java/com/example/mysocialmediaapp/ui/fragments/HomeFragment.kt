package com.example.mysocialmediaapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.FragmentHomeBinding
import com.example.mysocialmediaapp.ui.adapters.PostAdapter
import com.example.mysocialmediaapp.ui.adapters.StoryAdapter
import com.example.mysocialmediaapp.ui.models.Post
import com.example.mysocialmediaapp.ui.models.StoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var storyList: ArrayList<StoryModel> = ArrayList()
    private var postList: ArrayList<Post> = ArrayList()

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
    }
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

    private fun setUpStoryRecyclerView() {
        // Example for checking that our RecyclerView works fine
        storyList.add(
            StoryModel(
                R.drawable.cute_dog,
                R.drawable.live_icon,
                R.drawable.ic_add,
                "Cool"
            )
        )
        storyList.add(
            StoryModel(
                R.drawable.cute_dog,
                R.drawable.live_icon,
                R.drawable.ic_add,
                "Cool"
            )
        )
        storyList.add(
            StoryModel(
                R.drawable.cute_dog,
                R.drawable.live_icon,
                R.drawable.ic_add,
                "Cool"
            )
        )
        storyList.add(
            StoryModel(
                R.drawable.cute_dog,
                R.drawable.live_icon,
                R.drawable.ic_add,
                "Cool"
            )
        )
        // in future remove this custom added data

        val mAdapter = StoryAdapter(requireContext(), storyList)

        binding.storyRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.storyRecyclerView.isNestedScrollingEnabled = false
        binding.storyRecyclerView.adapter = mAdapter
    }
    private fun setUpDashBoardRecyclerView() {

        binding.dashBoardRV.layoutManager = LinearLayoutManager(requireContext())
        binding.storyRecyclerView.isNestedScrollingEnabled = false
        val postAdapter = PostAdapter(requireContext(), postList)
        binding.dashBoardRV.adapter = postAdapter

        firebaseDatabase.reference.child("posts")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    postList.clear()
                   for (dataSnapshot in snapshot.children){
                       val post = dataSnapshot.getValue(Post::class.java)!!
                       post.postID = dataSnapshot.key
                       postList.add(post)
                   }
                    postAdapter.notifyDataSetChanged()
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