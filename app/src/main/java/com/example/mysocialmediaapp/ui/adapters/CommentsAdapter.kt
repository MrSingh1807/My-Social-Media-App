package com.example.mysocialmediaapp.ui.adapters

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.CommentsSampleBinding
import com.example.mysocialmediaapp.ui.models.Comment
import com.example.mysocialmediaapp.ui.models.User
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


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

        val text = comment.commentedAt?.let { TimeAgo.using(it) }
        holder.binding.commentTimeTV.text = text

        FirebaseDatabase.getInstance().reference.child("Users")
            .child(comment.commentedBy!!).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)!!
                    Picasso.get().load(user.profilePhoto)
                        .placeholder(R.drawable.ic_image_search)
                        .into(holder.binding.profileImgVw)

                    val source =  Html.fromHtml("<b>" + user.name + "</b>").toString()
                    holder.binding.commentTV.text = source + " \n " + comment.commentBody

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}