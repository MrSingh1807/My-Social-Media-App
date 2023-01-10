package com.example.mysocialmediaapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.databinding.StoryRvDesignBinding
import com.example.mysocialmediaapp.ui.models.StoryModel


class StoryAdapter(
    val context: Context,
    private var storyModel : ArrayList<StoryModel> = ArrayList()
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {



    class StoryViewHolder(val binding: StoryRvDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val itemBinding =
            StoryRvDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.binding.storyImgVw.setImageResource(storyModel[position].story)
        holder.binding.profileImgVw.setImageResource(storyModel[position].profile)
        holder.binding.storyType.setImageResource(storyModel[position].storyType)
        holder.binding.nameStory.text = storyModel[position].nameStory
    }

    override fun getItemCount(): Int {
        return storyModel.size
    }
}