package org.marproject.githubuser.view.detail

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.koin.android.viewmodel.ext.android.viewModel
import org.marproject.githubuser.R
import org.marproject.githubuser.data.local.entity.User
import org.marproject.githubuser.data.local.sqlite.DatabaseContract.UserColumns.Companion.CONTENT_URI
import org.marproject.githubuser.data.network.response.UserResponse
import org.marproject.githubuser.databinding.ActivityDetailUserBinding
import org.marproject.githubuser.utils.adapter.SectionAdapter
import org.marproject.githubuser.utils.helpers.MappingHelper.mapRemoteModelToLocal

class DetailUserActivity : AppCompatActivity() {

    // binding
    private lateinit var binding: ActivityDetailUserBinding

    // viewModel
    private val viewModel: DetailViewModel by viewModel()

    // utils
    private var status = false
    private var userForFavorite = User()
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)

        // setup user
        val extra = intent.extras
        if (extra != null) {
            val id = extra.getInt(EXTRA_ID)
            val username = extra.getString(EXTRA_USER)

            if (username != null) {
                supportActionBar?.apply {
                    title = username
                    setDisplayHomeAsUpEnabled(true)
                }

                // init ui
                initUI(id, username)
            }
        }

        // set content view
        setContentView(binding.root)
    }

    private fun initUI(id: Int, username: String) {
        // setup adapter
        val sectionAdapter = SectionAdapter(this, supportFragmentManager)
        sectionAdapter.username = username
        with(binding.viewPager) {
            adapter = sectionAdapter
            binding.tabs.setupWithViewPager(this)
        }

        // fetch data
        viewModel.fetchData(username).observe(this, observer)

        // set uri
        uriWithId = Uri.parse("$CONTENT_URI/$id")

        // check is favorite
        viewModel.getUser(uriWithId).observe(this, favoriteObserver)
    }

    private val observer = Observer<UserResponse> { user ->
        if (user != null) {
            binding.tvProfile.text = user.profile
            binding.tvUsername.text = user.username

            if (user.name == "")
                binding.tvName.text = user.username
            else
                binding.tvName.text = user.name

            // avatars
            Glide.with(this)
                .load(user.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.imageAvatar)

            // create user
            userForFavorite = user.mapRemoteModelToLocal()

            // visible fab
            binding.fabFavorite.visibility = View.VISIBLE
        }
    }

    private val favoriteObserver = Observer<User> {
        // set status
        status = it.username != ""

        // status favorite
        setStatusFavorite(status)

        binding.fabFavorite.setOnClickListener {
            status = !status
            setStatusFavorite(status)

            if (status) {
                viewModel.insertFavorite(userForFavorite)
            } else {
                viewModel.deleteFavorite(uriWithId)
            }
        }
    }

    private fun setStatusFavorite(status: Boolean) {
        if (status)
            binding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorited
                )
            )
        else
            binding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite
                )
            )
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ID = "extra_id"
    }
}