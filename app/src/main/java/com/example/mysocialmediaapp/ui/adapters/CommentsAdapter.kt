package com.example.mysocialmediaapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.databinding.CommentsSampleBinding
import com.example.mysocialmediaapp.ui.models.Comment


class CommentsAdapter(
    val context: Context,
    private val commentList : ArrayList<Comment> = ArrayList()
) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    class CommentViewHolder(val binding: CommentsSampleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemBinding = CommentsSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentList[position]

        holder.binding.commentTV.text = comment.commentBody
        holder.binding.commentTimeTV.text = comment.commentedAt.toString()
    }
}