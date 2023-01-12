package com.example.mysocialmediaapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.FragmentProfileBinding
import com.example.mysocialmediaapp.ui.adapters.MyFriendProfileAdapter
import com.example.mysocialmediaapp.ui.models.MyFriendsProfileImageModel
import com.example.mysocialmediaapp.ui.models.StoryModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var myFriendsList: ArrayList<MyFriendsProfileImageModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        setFriendsListRecyclerView()

        return binding.root
    }

    private fun setFriendsListRecyclerView(){
        myFriendsList.add(MyFriendsProfileImageModel(R.drawable.cute_dog))
        myFriendsList.add(MyFriendsProfileImageModel(R.drawable.cute_dog))
        myFriendsList.add(MyFriendsProfileImageModel(R.drawable.cute_dog))
        myFriendsList.add(MyFriendsProfileImageModel(R.drawable.cute_dog))
        myFriendsList.add(MyFriendsProfileImageModel(R.drawable.cute_dog))
        myFriendsList.add(MyFriendsProfileImageModel(R.drawable.cute_dog))
        myFriendsList.add(MyFriendsProfileImageModel(R.drawable.cute_dog))

        val friendAdapter = MyFriendProfileAdapter(requireContext(), myFriendsList)
        binding.friendsProfileRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        binding.friendsProfileRV.isNestedScrollingEnabled = false
        binding.friendsProfileRV.adapter = friendAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}