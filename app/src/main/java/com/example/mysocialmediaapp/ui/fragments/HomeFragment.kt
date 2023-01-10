package com.example.mysocialmediaapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.FragmentHomeBinding
import com.example.mysocialmediaapp.ui.adapters.StoryAdapter
import com.example.mysocialmediaapp.ui.models.StoryModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var storyModel: ArrayList<StoryModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Example for checking that our RecyclerView works fine
        storyModel.add(
            StoryModel(
                R.drawable.cute_dog,
                R.drawable.live_icon,
                R.drawable.ic_add,
                "Cool"
            )
        )
        storyModel.add(
            StoryModel(
                R.drawable.cute_dog,
                R.drawable.live_icon,
                R.drawable.ic_add,
                "Cool"
            )
        )
        storyModel.add(
            StoryModel(
                R.drawable.cute_dog,
                R.drawable.live_icon,
                R.drawable.ic_add,
                "Cool"
            )
        )
        storyModel.add(
            StoryModel(
                R.drawable.cute_dog,
                R.drawable.live_icon,
                R.drawable.ic_add,
                "Cool"
            )
        )
        // in future remove this custom added data

        val mAdapter = StoryAdapter(requireContext(), storyModel)

        binding.storyRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.storyRecyclerView.isNestedScrollingEnabled = false
        binding.storyRecyclerView.adapter = mAdapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}