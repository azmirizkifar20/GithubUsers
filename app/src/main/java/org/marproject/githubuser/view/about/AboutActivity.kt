package org.marproject.githubuser.view.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import org.marproject.githubuser.R
import org.marproject.githubuser.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    // binding
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        supportActionBar?.hide()

        Glide.with(this)
            .load(R.drawable.profile)
            .circleCrop()
            .into(binding.imageProfile)

        setContentView(binding.root)
    }
}