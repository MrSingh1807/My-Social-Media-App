package com.example.mysocialmediaapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.databinding.FriendsRvInProfileBinding
import com.example.mysocialmediaapp.ui.models.FollowModel

class FollowerAdapter(
    val context: Context,
    private var myFriendsModel: ArrayList<FollowModel> = ArrayList()

) : RecyclerView.Adapter<FollowerAdapter.MyFriendProfileViewHolder>() {

    class MyFriendProfileViewHolder(val binding: FriendsRvInProfileBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFriendProfileViewHolder {
        val binding =
            FriendsRvInProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyFriendProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyFriendProfileViewHolder, position: Int) {
//        holder.binding.friendsImageIV.setImageResource(myFriendsModel[position].profileImage)
    }

    override fun getItemCount(): Int {
        return myFriendsModel.size
    }
}