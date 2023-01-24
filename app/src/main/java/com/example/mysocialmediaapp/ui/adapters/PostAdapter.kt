package com.example.mysocialmediaapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.DashboardRvSampleBinding
import com.example.mysocialmediaapp.ui.UI.CommentsActivity
import com.example.mysocialmediaapp.ui.models.NotificationModel
import com.example.mysocialmediaapp.ui.models.Post
import com.example.mysocialmediaapp.ui.models.User
import com.example.mysocialmediaapp.ui.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.Date

class PostAdapter(
    val context: Context,
    private var postModel: ArrayList<Post> = ArrayList(),
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<PostAdapter.DashBoardHolder>() {

    class DashBoardHolder(val binding: DashboardRvSampleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardHolder {
        val itemBinding =
            DashboardRvSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DashBoardHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return postModel.size
    }

    override fun onBindViewHolder(holder: DashBoardHolder, position: Int) {
        val post = postModel[position]
        Picasso.get().load(post.postImage)
            .placeholder(R.drawable.ic_image_search)
            .into(holder.binding.postImgVw)

        holder.binding.likesTV.text = post.postLikes.toString()
        holder.binding.commentTV.text = post.commentCount.toString()
        if (post.postDescription.equals("")) {
            holder.binding.postDescriptionTV.visibility = View.GONE
        } else {
            holder.binding.postDescriptionTV.text = post.postDescription
            holder.binding.postDescriptionTV.visibility = View.VISIBLE
        }


        mainViewModel.userFirebaseDB.child(post.postedBy!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)!!
                    Picasso.get().load(user.profilePhoto)
                        .placeholder(R.drawable.ic_image_search)
                        .into(holder.binding.profileImgVw)

                    holder.binding.userNameTV.text = user.name
                    holder.binding.aboutTV.text = user.profession

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        val firebasePostInstance = mainViewModel.postFirebaseDB.child(post.postID!!)
        val currentUserUID = mainViewModel.uid!!

        firebasePostInstance.child("likes")
            .child(currentUserUID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        holder.binding.likeImgVw.setImageResource(R.drawable.ic_pink_heart)
                    } else {
                        holder.binding.likeImgVw.setOnClickListener {
                            firebasePostInstance.child("likes")
                                .child(currentUserUID)
                                .setValue(true).addOnSuccessListener {
                                    firebasePostInstance.child("postLikes")
                                        .setValue(post.postLikes?.plus(1)).addOnSuccessListener {
                                            holder.binding.likeImgVw.setImageResource(R.drawable.ic_pink_heart)
                                            val notification = NotificationModel(
                                                notificationBy = currentUserUID,
                                                notificationAt = Date().time,
                                                postID = post.postID,
                                                postedBy = post.postedBy,
                                                type = "like"
                                            )

                                            mainViewModel.notificationFirebaseDB.child(post.postID!!)
                                                .push()
                                                .setValue(notification)
                                        }
                                }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        holder.binding.commentImgVw.setOnClickListener {
            val intent = Intent(context, CommentsActivity::class.java)
            intent.putExtra("postID", post.postID)
            intent.putExtra("postedBy", post.postedBy)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

    }

}