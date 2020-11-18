package org.marproject.consumerapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.marproject.consumerapp.R
import org.marproject.consumerapp.data.User
import org.marproject.consumerapp.databinding.ActivityMainBinding
import org.marproject.consumerapp.utils.FavoriteAdapter

class MainActivity : AppCompatActivity() {

    // binding
    private lateinit var binding: ActivityMainBinding

    // view model
    private val viewModel: MainViewModel by viewModel()

    // adapter
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
        }

        // init adapter
        adapter = FavoriteAdapter(this)

        // setup adapter
        with(binding.recyclerviewUser) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = this@MainActivity.adapter
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
}