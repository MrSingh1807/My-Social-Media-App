package com.example.mysocialmediaapp.ui.fragments.notificationfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.FragmentRequestBinding
import com.example.mysocialmediaapp.databinding.FragmentTabNotificationBinding
import com.example.mysocialmediaapp.ui.adapters.NotificationAdapter
import com.example.mysocialmediaapp.ui.models.NotificationModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabNotificationFragment : Fragment() {

    private var _binding: FragmentTabNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth

    private val notificationLists: ArrayList<NotificationModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
    }

      override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
          _binding = FragmentTabNotificationBinding.inflate(inflater, container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onResume() {
        super.onResume()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

        val notificationAdapter = NotificationAdapter(requireContext(), notificationLists)
        binding.tabNotificationRV.layoutManager = LinearLayoutManager(requireContext())
        binding.tabNotificationRV.adapter = notificationAdapter

        firebaseDatabase.reference.child("Notification")
            .child(firebaseAuth.uid!!).addValueEventListener( object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    notificationLists.clear()
                    for (dataSnapShot in snapshot.children){
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}