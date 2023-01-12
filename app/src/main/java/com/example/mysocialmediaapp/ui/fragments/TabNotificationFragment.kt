package com.example.mysocialmediaapp.ui.fragments

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

class TabNotificationFragment : Fragment() {

    private var _binding: FragmentTabNotificationBinding? = null
    private val binding get() = _binding!!

    private val notificationLists: ArrayList<NotificationModel> = ArrayList()
      override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
          _binding = FragmentTabNotificationBinding.inflate(inflater, container,false)

          setUpRecyclerView()
        return binding.root
    }

    private fun setUpRecyclerView() {
        notificationLists.add(NotificationModel(R.drawable.kid, "<b>Mr Singh</b>, Cool Buddy", "20 minutes Ago"))
        notificationLists.add(NotificationModel(R.drawable.kid, "<b>Mr Singh</b>, Cool Buddy", "20 minutes Ago"))
        notificationLists.add(NotificationModel(R.drawable.kid, "<b>Mr Singh</b>, Cool Buddy", "20 minutes Ago"))
        notificationLists.add(NotificationModel(R.drawable.kid, "<b>Mr Singh</b>, Cool Buddy", "20 minutes Ago"))
        notificationLists.add(NotificationModel(R.drawable.kid, "<b>Mr Singh</b>, Cool Buddy", "20 minutes Ago"))
        notificationLists.add(NotificationModel(R.drawable.kid, "<b>Mr Singh</b>, Cool Buddy", "20 minutes Ago"))
        notificationLists.add(NotificationModel(R.drawable.kid, "<b>Mr Singh</b>, Cool Buddy", "20 minutes Ago"))
        notificationLists.add(NotificationModel(R.drawable.kid, "<b>Mr Singh</b>, Cool Buddy", "20 minutes Ago"))


        val adapter = NotificationAdapter(requireContext(), notificationLists)
        binding.tabNotificationRV.layoutManager = LinearLayoutManager(requireContext())
        binding.tabNotificationRV.adapter = adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}