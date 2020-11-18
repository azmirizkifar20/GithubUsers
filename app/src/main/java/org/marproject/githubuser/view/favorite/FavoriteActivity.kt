package org.marproject.githubuser.view.favorite

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_favorite.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.marproject.githubuser.R
import org.marproject.githubuser.data.local.entity.User
import org.marproject.githubuser.databinding.ActivityFavoriteBinding
import org.marproject.githubuser.utils.adapter.FavoriteAdapter
import org.marproject.githubuser.view.setting.SettingActivity

class FavoriteActivity : AppCompatActivity() {

    // binding
    private lateinit var binding: ActivityFavoriteBinding

    // view model
    private val viewModel: FavoriteViewModel by viewModel()

    // adapter
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        supportActionBar?.apply {
            title = getString(R.string.favorite_user)
            setDisplayHomeAsUpEnabled(true)
        }

        // init adapter
        adapter = FavoriteAdapter(this)

        // setup adapter
        with(binding.recyclerviewUser) {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            setHasFixedSize(true)
            adapter = this@FavoriteActivity.adapter
        }

        viewModel.user.observe(this, observer)

        setContentView(binding.root)
    }

    private val observer = Observer<List<User>> {
        if (it.isNotEmpty())
            adapter.setUsers(it)
        else
            view_empty.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.setting -> {
            startActivity(Intent(this, SettingActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}