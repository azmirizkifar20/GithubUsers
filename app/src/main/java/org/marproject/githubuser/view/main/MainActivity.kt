package org.marproject.githubuser.view.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import org.marproject.githubuser.R
import org.marproject.githubuser.data.network.response.UserResponse
import org.marproject.githubuser.databinding.ActivityMainBinding
import org.marproject.githubuser.utils.adapter.UserAdapter
import org.marproject.githubuser.view.favorite.FavoriteActivity
import org.marproject.githubuser.view.setting.SettingActivity

class MainActivity : AppCompatActivity() {

    // binding
    private lateinit var binding: ActivityMainBinding

    // viewModel
    private val viewModel: MainViewModel by viewModel()

    // util
    private lateinit var adapter: UserAdapter
    private val users = arrayListOf<UserResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // setup toolbar
        setupToolbar()

        // init adapter
        adapter = UserAdapter()

        // init ui
        initUI()

        // set content view
        setContentView(binding.root)
    }

    private fun initUI() {
        // setup adapter
        with(binding.recyclerviewUser) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = this@MainActivity.adapter
        }

        // fetch data
        viewModel.fetchData().observe(this, observer)

        // loading
        viewModel.isLoading.observe(this, {
            if (it == false) binding.loading.visibility = View.GONE
            else binding.loading.visibility = View.VISIBLE
        })
    }

    private val observer = Observer<List<UserResponse>> {
        if (it != null) {
            adapter.setUsers(it)
            if (users.isEmpty()) {
                users.addAll(it)
            }
        }
    }

    // Setup Toolbar
    private fun setupToolbar() {
        supportActionBar?.apply {
            this.title = getString(R.string.github_user)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        with(searchView) {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = resources.getString(R.string.search)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null){
                        adapter.setUsers(listOf())
                        viewModel.searchUser(query).observe(this@MainActivity, observer)
                    }
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    if (query?.trim() == "") {
                        adapter.setUsers(listOf())
                    }
                    return false
                }

            })
        }

        menu.findItem(R.id.search).setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                adapter.setUsers(listOf())
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                viewModel.fetchData().observe(this@MainActivity, observer)
                return true
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> startActivity(Intent(this, FavoriteActivity::class.java))
            R.id.setting -> startActivity(Intent(this, SettingActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList("users", users)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val users = savedInstanceState.getParcelableArray("users")
        users?.let { adapter.setUsers(it as List<UserResponse>) }
    }
}