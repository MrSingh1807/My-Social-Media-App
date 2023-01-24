package com.example.mysocialmediaapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.StoryRvDesignBinding
import com.example.mysocialmediaapp.ui.models.StoryModel
import com.example.mysocialmediaapp.ui.models.User
import com.example.mysocialmediaapp.ui.viewmodels.MainViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import dagger.hilt.android.internal.managers.ViewComponentManager
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.callback.StoryClickListeners
import omari.hamza.storyview.model.MyStory


class StoryAdapter(
    val context: Context,
    private var storyModel: ArrayList<StoryModel> = ArrayList(),
    private val mainViewModel: MainViewModel
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
        if (story.stories?.size!! > 0) {
            val lastStory = story.stories[story.stories.size - 1]
            Picasso.get().load(lastStory.image)
                .placeholder(R.drawable.ic_image_search)
                .into(holder.binding.storyImgVw)

            holder.binding.statusCircleCSV.setPortionsCount(story.stories.size)

            mainViewModel.userFirebaseDB.child(story.storyBy!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)!!
                        Picasso.get().load(user.profilePhoto)
                            .placeholder(R.drawable.ic_image_search)
                            .into(holder.binding.profileImgVw)

                        holder.binding.nameStory.text = user.name
                        holder.binding.storyImgVw.setOnClickListener {

                            val myStories: ArrayList<MyStory> = ArrayList();

                            for (story in story.stories) {
                                myStories.add(MyStory(story.image))
                            }

                            val mContext =
                                if (context is ViewComponentManager.FragmentContextWrapper) {
                                    context.baseContext
                                } else {
                                    context
                                }

                            val appCompactActivity = mContext as AppCompatActivity
                            StoryView.Builder(appCompactActivity.supportFragmentManager)
                                .setStoriesList(myStories) // Required
                                .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                .setTitleText(user.name) // Default is Hidden
                                .setSubtitleText("") // Default is Hidden
                                .setTitleLogoUrl(user.profilePhoto) // Default is Hidden
                                .setStoryClickListeners(object : StoryClickListeners {
                                    override fun onDescriptionClickListener(position: Int) {
                                        //your action
                                    }

                                    override fun onTitleIconClickListener(position: Int) {
                                        //your action
                                    }
                                }) // Optional Listeners
                                .build() // Must be called before calling show method
                                .show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    override fun getItemCount(): Int {
        return storyModel.size
    }
}