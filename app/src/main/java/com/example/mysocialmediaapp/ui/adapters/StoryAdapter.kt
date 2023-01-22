package com.example.mysocialmediaapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.StoryRvDesignBinding
import com.example.mysocialmediaapp.ui.models.StoryModel
import com.example.mysocialmediaapp.ui.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


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
       val story = storyModel[position]
        val lastStory = story.stories?.get(story.stories.size - 1)!!
        Picasso.get().load(lastStory.image)
            .placeholder(R.drawable.ic_image_search)
            .into(holder.binding.storyImgVw)

        holder.binding.statusCircleCSV.setPortionsCount(story.stories.size)

        FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(story.storyBy!!).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)!!
                    Picasso.get().load(user.profilePhoto)
                        .placeholder(R.drawable.ic_image_search)
                        .into(holder.binding.profileImgVw)

                    holder.binding.nameStory.text = user.name
                    holder.binding.storyImgVw.setOnClickListener {

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun getItemCount(): Int {
        return storyModel.size
    }
}