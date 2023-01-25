package com.example.mysocialmediaapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialmediaapp.databinding.FragmentNotificationBinding
import com.example.mysocialmediaapp.ui.adapters.NotificationAdapter
import com.example.mysocialmediaapp.ui.adapters.NotificationClickListener
import com.example.mysocialmediaapp.ui.models.NotificationModel
import com.example.mysocialmediaapp.ui.viewmodels.MainViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!


    private val mainViewModel by viewModels<MainViewModel>()

    private val notificationLists: ArrayList<NotificationModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)

        setUpRecyclerView()

        return binding.root
    }

    private fun setUpRecyclerView() {

        val notificationAdapter = NotificationAdapter(requireContext(), notificationLists, mainViewModel,
            object : NotificationClickListener {
                override fun onClick(post: NotificationModel) {
                    val a = CommentBottomSheetFragment()
                    a.postID = post.postID!!
                    a.postedBy = post.postedBy!!
                    a.show(requireActivity().supportFragmentManager, "xyz")
                }
            })

        binding.tabNotificationRV.layoutManager = LinearLayoutManager(requireContext())
        binding.tabNotificationRV.adapter = notificationAdapter

        mainViewModel.notificationFirebaseDB
            .child(mainViewModel.uid!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    notificationLists.clear()
                    for (dataSnapShot in snapshot.children) {
                        val notification = dataSnapShot.getValue(NotificationModel::class.java)!!
                        notification.notificationId = dataSnapShot.key

                        notificationLists.add(notification)
                        notificationAdapter.notifyDataSetChanged()
                    }
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