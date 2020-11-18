package org.marproject.githubuser.utils.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_user.view.*
import org.marproject.githubuser.R
import org.marproject.githubuser.data.network.response.UserResponse
import org.marproject.githubuser.view.detail.DetailUserActivity

class UserAdapter : RecyclerView.Adapter<UserAdapter.MainViewHolder>() {

    private val users = ArrayList<UserResponse>()

    fun setUsers(users: List<UserResponse>?) {
        if (users == null) return
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MainViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
    )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = users.size

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserResponse) {
            with(itemView) {
                tv_username.text = user.username
                tv_profile.text = user.profile

                Glide.with(context)
                    .load(user.avatar)
                    .apply(RequestOptions.circleCropTransform())
                    .into(image_avatar)

                setOnClickListener {
                    val intent = Intent(context, DetailUserActivity::class.java).apply {
                        putExtra(DetailUserActivity.EXTRA_USER, user.username)
                        putExtra(DetailUserActivity.EXTRA_ID, user.id)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
}