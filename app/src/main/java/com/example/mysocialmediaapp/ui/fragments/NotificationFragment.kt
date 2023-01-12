package com.example.mysocialmediaapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.FragmentAddBinding
import com.example.mysocialmediaapp.databinding.FragmentNotificationBinding
import com.example.mysocialmediaapp.ui.adapters.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private var _binding : FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)

        binding.notificationViewPager.adapter = fragmentManager?.let { ViewPagerAdapter(it) }
        binding.notificationTabLayout.setupWithViewPager(binding.notificationViewPager)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}