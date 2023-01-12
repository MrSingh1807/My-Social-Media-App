package com.example.mysocialmediaapp.ui.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.databinding.TabNotificationRecyclerViewBinding
import com.example.mysocialmediaapp.ui.models.NotificationModel


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
        holder.binding.profileImgVw.setImageResource(notificationModel[position].profileImage)
        holder.binding.nameAndAboutTV.text = Html.fromHtml(notificationModel[position].notification)
        holder.binding.timeTV.text = notificationModel[position].time
    }

    override fun getItemCount(): Int {
        return notificationModel.size
    }
}