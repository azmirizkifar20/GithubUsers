package org.marproject.githubuser.utils.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_user.view.*
import org.marproject.githubuser.R
import org.marproject.githubuser.data.local.entity.User
import org.marproject.githubuser.view.detail.DetailUserActivity

class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val users = ArrayList<User>()

    fun setUsers(users: List<User>?) {
        if (users == null) return
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FavoriteViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
    )

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user, position)
    }

    override fun getItemCount(): Int = users.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User, position: Int) {
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
                    activity.startActivity(intent)
                }
            }
        }
    }
}