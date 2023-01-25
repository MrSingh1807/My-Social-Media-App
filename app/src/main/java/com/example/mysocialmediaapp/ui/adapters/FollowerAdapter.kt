package com.example.mysocialmediaapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.FriendsRvInProfileBinding
import com.example.mysocialmediaapp.ui.models.FollowModel
import com.example.mysocialmediaapp.ui.models.User
import com.example.mysocialmediaapp.ui.viewmodels.MainViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class FollowerAdapter(
    val context: Context,
    private var myFriendsModel: ArrayList<FollowModel> = ArrayList(),
    private val mainViewModel: MainViewModel

) : RecyclerView.Adapter<FollowerAdapter.MyFriendProfileViewHolder>() {

    class MyFriendProfileViewHolder(val binding: FriendsRvInProfileBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFriendProfileViewHolder {
        val binding =
            FriendsRvInProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyFriendProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyFriendProfileViewHolder, position: Int) {

        val friendsList = myFriendsModel[position]
        mainViewModel.userFirebaseDB
            .child(friendsList.followedBy).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   val user = snapshot.getValue(User::class.java)!!
                    Picasso.get()
                        .load(user.profilePhoto)
                        .placeholder(R.drawable.ic_image_search)
                        .into(holder.binding.friendsImageIV)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun getItemCount(): Int {
        return myFriendsModel.size
    }
}