package com.example.mysubmission2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mysubmission2.databinding.UserItemFollowBinding

class FollowerAdapter(private val listUser: ArrayList<UserData>) :
    RecyclerView.Adapter<FollowerAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        val binding =
            UserItemFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    inner class ListViewHolder(private val binding: UserItemFollowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserData) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(80, 80))
                    .into(civAvatarFol)
                tvUsernameFoll.text = user.username
                tvNameFoll.text = user.name
            }

        }

    }
}