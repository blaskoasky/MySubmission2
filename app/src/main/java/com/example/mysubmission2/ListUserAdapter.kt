package com.example.mysubmission2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mysubmission2.databinding.UserItemBinding



class ListUserAdapter (private val listUser: ArrayList<UserData>):
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    lateinit var context: Context
    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size


    inner class ListViewHolder (private val binding: UserItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserData) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(100,100))
                    .into(civAvatar)
                tvUsername.text = user.username
                tvName.text = user.name
                tvCompany.text = user.company
                tvLocation.text = user.location

                itemView.setOnClickListener {
                    val uData = UserData(
                        user.avatar,
                        user.username,
                        user.name,
                        user.company,
                        user.location
                    )
                    val intent = Intent(context, UserDetailActivity::class.java)
                    intent.putExtra(UserDetailActivity.EXTRA_DATA, uData)
                    context.startActivity(intent)
                }

            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserData)
    }
}