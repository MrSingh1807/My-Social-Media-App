package com.example.mysocialmediaapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.SearchUserSampleBinding
import com.example.mysocialmediaapp.ui.models.FollowModel
import com.example.mysocialmediaapp.ui.models.NotificationModel
import com.example.mysocialmediaapp.ui.models.User
import com.example.mysocialmediaapp.ui.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.Date


class SearchUserAdapter(
    val context: Context,
    private var user: ArrayList<User> = ArrayList(),
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder>() {

    class SearchUserViewHolder(val binding: SearchUserSampleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val itemBinding =
            SearchUserSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SearchUserViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        holder.binding.userNameTV.text = user[position].name
        holder.binding.userProfessionTV.text = user[position].profession

        if (user[position].profilePhoto!!.isNotEmpty()) {
            Picasso.get().load(user[position].profilePhoto!!)
                .placeholder(R.drawable.ic_image_search)
                .into(holder.binding.profileImgVw)
        }

        mainViewModel.userFirebaseDB
            .child(user[position].userID!!)
            .child("followers")
            .child(mainViewModel.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        holder.binding.followBTN.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.follow_active_btn
                            )
                        )
                        holder.binding.followBTN.text = "Following"
                        holder.binding.followBTN.setTextColor(context.resources.getColor(R.color.mediumGray))
                        holder.binding.followBTN.isEnabled = false
                    } else {
                        holder.binding.followBTN.setOnClickListener {

                            val currentUserUID = mainViewModel.uid
                            val follow = FollowModel(currentUserUID, Date().time)

                            mainViewModel.userFirebaseDB
                                .child(user[position].userID!!)
                                .child("followers")
                                .child(currentUserUID)
                                .setValue(follow)
                                .addOnSuccessListener {
                                    mainViewModel.userFirebaseDB
                                        .child(user[position].userID!!)
                                        .child("followerCount")
                                        .setValue(user[position].followerCount?.plus(1))
                                        .addOnSuccessListener {
                                            holder.binding.followBTN.setBackgroundDrawable(
                                                ContextCompat.getDrawable(
                                                    context,
                                                    R.drawable.follow_active_btn
                                                )
                                            )
                                            holder.binding.followBTN.text = "Following"
                                            holder.binding.followBTN.setTextColor(
                                                context.resources.getColor(
                                                    R.color.mediumGray
                                                )
                                            )
                                            holder.binding.followBTN.isEnabled = false

                                            Toast.makeText(
                                                context,
                                                "You followed - ${user[position].name}",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            val notification = NotificationModel(
                                                notificationBy = currentUserUID,
                                                notificationAt = Date().time,
                                                type = "follow"
                                            )
                                            mainViewModel.notificationFirebaseDB
                                                .child(user[position].userID!!).push()
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
    }
}