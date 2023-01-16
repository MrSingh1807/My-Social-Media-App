package com.example.mysocialmediaapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.databinding.DashboardRvSampleBinding
import com.example.mysocialmediaapp.ui.models.DashBoardModel

class DashBoardAdapter(
    val context: Context,
    private var dashBoardModel: ArrayList<DashBoardModel> = ArrayList()
) : RecyclerView.Adapter<DashBoardAdapter.DashBoardHolder>() {

    class DashBoardHolder(val binding: DashboardRvSampleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardHolder {
        val itemBinding =
            DashboardRvSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DashBoardHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: DashBoardHolder, position: Int) {
        holder.binding.profileImgVw.setImageResource(dashBoardModel[position].profile)
        holder.binding.userNameTV.text = dashBoardModel[position].name
        holder.binding.aboutTV.text = dashBoardModel[position].about

        holder.binding.storyImgVw.setImageResource(dashBoardModel[position].postImage)
        holder.binding.likesTV.text = dashBoardModel[position].likes
        holder.binding.commentTV.text = dashBoardModel[position].comments
        holder.binding.shareTV.text = dashBoardModel[position].share

    }

    override fun getItemCount(): Int {
        return dashBoardModel.size
    }

}