package com.example.mysocialmediaapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialmediaapp.databinding.FragmentSearchBinding
import com.example.mysocialmediaapp.ui.adapters.SearchUserAdapter
import com.example.mysocialmediaapp.ui.models.User
import com.example.mysocialmediaapp.ui.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    var userList = ArrayList<User>()
    private val mainViewModel by viewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        followerUsersRecyclerView()

        return binding.root
    }

    fun followerUsersRecyclerView(){
        binding.usersRV.layoutManager = LinearLayoutManager(context)
        val mAdapter = SearchUserAdapter(requireContext(), userList, mainViewModel)
        binding.usersRV.adapter = mAdapter

        mainViewModel.userFirebaseDB.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for ( DataSnapShot in snapshot.children){
                    val user = DataSnapShot.getValue(User::class.java)
                    user?.userID = DataSnapShot.key

                    if (DataSnapShot.key != FirebaseAuth.getInstance().uid)
                        userList.add(user!!)
                }
                mAdapter.notifyDataSetChanged()
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