package com.example.mysocialmediaapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysocialmediaapp.R
import com.example.mysocialmediaapp.databinding.FragmentCommentBottomSheetBinding
import com.example.mysocialmediaapp.ui.adapters.CommentsAdapter
import com.example.mysocialmediaapp.ui.models.Comment
import com.example.mysocialmediaapp.ui.models.NotificationModel
import com.example.mysocialmediaapp.ui.models.Post
import com.example.mysocialmediaapp.ui.models.User
import com.example.mysocialmediaapp.ui.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CommentBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCommentBottomSheetBinding? = null
    private val binding get() = _binding!!

    lateinit var postID: String
    lateinit var postedBy: String

    private val commentsList: ArrayList<Comment> = ArrayList()
    private val mainViewModel by viewModels<MainViewModel>()

    private val commentsAdapter by lazy { CommentsAdapter(commentsList) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCommentBottomSheetBinding.inflate(inflater, container, false)


        mainViewModel.postFirebaseDB
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

        mainViewModel.userFirebaseDB
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
                FirebaseAuth.getInstance().uid
            )


            mainViewModel.postFirebaseDB.child(postID)
                .child("comments")
                .push()
                .setValue(comment).addOnSuccessListener {
                    mainViewModel.postFirebaseDB.child(postID)
                        .child("commentCount")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var commentCount = 0
                                if (snapshot.exists()) {
                                    commentCount = snapshot.getValue(Int::class.java)!!
                                }
                                mainViewModel.postFirebaseDB.child(postID)
                                    .child("commentCount")
                                    .setValue(commentCount + 1).addOnSuccessListener {
                                        binding.postCommentET.setText("")
                                        Toast.makeText(
                                            context,
                                            "Commented",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        val notification = NotificationModel(
                                            notificationBy = mainViewModel.uid,
                                            notificationAt = Date().time,
                                            postID = postID,
                                            postedBy = postedBy,
                                            type = "comment"
                                        )

                                        mainViewModel.notificationFirebaseDB.child(postedBy).push()
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


        return binding.root
    }

    private fun setUpCommentsRecyclerView() {


        binding.commentsRV.layoutManager = LinearLayoutManager(requireContext())
        binding.commentsRV.adapter = commentsAdapter

        mainViewModel.postFirebaseDB.child(postID)
            .child("comments").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    commentsList.clear()
                    for (dataSnapShot in snapshot.children) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}