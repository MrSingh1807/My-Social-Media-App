package com.example.mysocialmediaapp.ui.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.TabNotificationRecyclerViewBinding
import com.example.mysocialmediaapp.ui.models.NotificationModel
import com.example.mysocialmediaapp.ui.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class NotificationAdapter(
    val context: Context,
    private var notificationModel: ArrayList<NotificationModel> = ArrayList()
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(val binding: TabNotificationRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val itemBind = TabNotificationRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(itemBind)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationModel[position]

        val firebaseDatabase = FirebaseDatabase.getInstance().reference
        firebaseDatabase.child("Users")
            .child(notification.notificationBy!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)!!
                    Picasso.get().load(user.profilePhoto)
                        .placeholder(R.drawable.ic_image_search)
                        .into(holder.binding.profileImgVw)

                    when (notification.type) {
                        "like" -> holder.binding.nameAndAboutTV.text = Html.fromHtml("<b>${user.name}<b>  Liked your post")
                        "comment" -> holder.binding.nameAndAboutTV.text = Html.fromHtml("<b>${user.name}<b>  Commented on your post")
                        "follow" -> holder.binding.nameAndAboutTV.text = Html.fromHtml("<b>${user.name}<b>  start following you")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun getItemCount(): Int {
        return notificationModel.size
    }
}