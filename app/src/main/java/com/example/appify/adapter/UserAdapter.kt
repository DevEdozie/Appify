package com.example.appify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appify.database.model.User
import com.example.appify.databinding.UserItemBinding


// Adapter class for displaying a list of users in a RecyclerView
class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    // ViewHolder class to hold the view for each user item
    class UserViewHolder(val itemBinding: UserItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    // DiffUtil callback to efficiently update the list of users
    private val differCallBack = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            // Check if the IDs of the items are the same to determine if they represent the same item
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            // Check if the content of the items is the same to determine if they are identical
            return oldItem == newItem
        }
    }

    // AsyncListDiffer to handle list changes asynchronously and efficiently
    val differ = AsyncListDiffer(this, differCallBack)

    // Inflate the user item layout and create a ViewHolder for it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    // Return the total number of tasks in the current list
    override fun getItemCount() = differ.currentList.size

    // Bind the data to the views in each task item
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = differ.currentList[position]

        // Set the user name
        holder.itemBinding.userName.text = currentUser.name

    }
}
