package com.example.mysocialmediaapp.ui.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.ActivityCommentsBinding
import com.example.mysocialmediaapp.ui.adapters.CommentsAdapter
import com.example.mysocialmediaapp.ui.models.Comment
import com.example.mysocialmediaapp.ui.models.NotificationModel
import com.example.mysocialmediaapp.ui.models.Post
import com.example.mysocialmediaapp.ui.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.Date

class CommentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentsBinding
    lateinit var postID: String
    lateinit var postedBy: String

    private val commentsList : ArrayList<Comment> = ArrayList()

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        this@CommentsActivity.title = "Comments"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        postID = intent.getStringExtra("postID").toString()
        postedBy = intent.getStringExtra("postedBy").toString()

        firebaseDatabase.reference.child("posts")
            .child(postID).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val post = snapshot.getValue(Post::class.java)!!
                    Picasso.get().load(post.postImage)
                        .placeholder(R.drawable.ic_image_search)
                        .into(binding.postImgIV)

                    binding.descriptionTV.text = post.postDescription
                    binding.likesTV.text = post.postLikes.toString()
                    binding.commentTV.text = post.commentCount.toString()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        firebaseDatabase.reference.child("Users")
            .child(postedBy).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)!!
                    Picasso.get().load(user.profilePhoto)
                        .placeholder(R.drawable.ic_image_search)
                        .into(binding.profileImgVw)

                    binding.userNameTV.text = user.name
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        binding.postCommentIcon.setOnClickListener {
            val comment = Comment(
                binding.postCommentET.text.toString(),
                Date().time,
                FirebaseAuth.getInstance().uid )


            firebaseDatabase.reference.child("posts")
                .child(postID)
                .child("comments")
                .push()
                .setValue(comment).addOnSuccessListener {
                    firebaseDatabase.reference.child("posts")
                        .child(postID)
                        .child("commentCount").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var commentCount = 0
                                if (snapshot.exists()){
                                    commentCount = snapshot.getValue(Int::class.java)!!
                                }
                                firebaseDatabase.reference.child("posts")
                                    .child(postID)
                                    .child("commentCount")
                                    .setValue(commentCount+1).addOnSuccessListener {
                                        binding.postCommentET.setText("")
                                        Toast.makeText(this@CommentsActivity, "Commented", Toast.LENGTH_SHORT).show()

                                        val notification = NotificationModel(
                                            notificationBy = firebaseAuth.uid,
                                            notificationAt = Date().time,
                                            postID = postID,
                                            postedBy = postedBy,
                                            type = "comment"
                                        )

                                        firebaseDatabase.reference.child("Notification")
                                            .child(postedBy).push()
                                            .setValue(notification)
                                    }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                }
        }

        setUpCommentsRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun setUpCommentsRecyclerView() {

        val commentsAdapter = CommentsAdapter(this,commentsList)
        binding.commentsRV.layoutManager = LinearLayoutManager(this)
        binding.commentsRV.adapter = commentsAdapter

        firebaseDatabase.reference.child("posts")
            .child(postID)
            .child("comments").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    commentsList.clear()
                    for (dataSnapShot in snapshot.children){
                        val comment = dataSnapShot.getValue(Comment::class.java)!!
                        commentsList.add(comment)
                    }
                    commentsAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}