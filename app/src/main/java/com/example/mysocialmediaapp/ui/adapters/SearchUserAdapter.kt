package com.example.mysocialmediaapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.SearchUserSampleBinding
import com.example.mysocialmediaapp.ui.models.FollowModel
import com.example.mysocialmediaapp.ui.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import java.util.Date


class SearchUserAdapter(
    val context: Context,
    private var user: ArrayList<User> = ArrayList()
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

        holder.binding.followBTN.setOnClickListener {

            val currentUserUID = FirebaseAuth.getInstance().uid!!
            val follow = FollowModel(currentUserUID, Date().time)

            FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(user[position].userID!!)
                .child("followers")
                .child(currentUserUID)
                .setValue(follow)
                .addOnSuccessListener {
                    FirebaseDatabase.getInstance().reference
                        .child("Users")
                        .child(user[position].userID!!)
                        .child("followerCount")
                        .setValue(user[position].followerCount?.plus(1))
                        .addOnSuccessListener {
                            Toast.makeText(context, "You followed - ${user[position].name}", Toast.LENGTH_SHORT).show()
                        }
                }
        }

    }
}