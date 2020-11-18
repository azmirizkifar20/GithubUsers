package org.marproject.githubuser.view.favorite

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.marproject.githubuser.R
import org.marproject.githubuser.data.local.sqlite.DatabaseContract.UserColumns.Companion.CONTENT_URI
import org.marproject.githubuser.databinding.ActivityFavoriteBinding
import org.marproject.githubuser.utils.adapter.FavoriteAdapter
import org.marproject.githubuser.utils.helpers.MappingHelper

class FavoriteActivity : AppCompatActivity() {

    // binding
    private lateinit var binding: ActivityFavoriteBinding

    // adapter
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        supportActionBar?.apply {
            title = getString(R.string.favorite_user)
            setDisplayHomeAsUpEnabled(true)
        }

        // setup content resolver
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUsersAsync()
            }
        }

        Log.i("testing", CONTENT_URI.toString())
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        // init adapter
        adapter = FavoriteAdapter(this)

        // setup adapter
        with(binding.recyclerviewUser) {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            setHasFixedSize(true)
            adapter = this@FavoriteActivity.adapter
        }

        loadUsersAsync()

        setContentView(binding.root)
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUsers = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val users = deferredUsers.await()
            if (users.size > 0) {
                adapter.setUsers(users)
            } else {
                view_empty.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}