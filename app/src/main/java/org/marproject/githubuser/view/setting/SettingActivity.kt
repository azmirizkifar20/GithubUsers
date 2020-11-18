package org.marproject.githubuser.view.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import org.marproject.githubuser.R
import org.marproject.githubuser.databinding.ActivitySettingBinding
import org.marproject.githubuser.view.about.AboutActivity

class SettingActivity : AppCompatActivity() {

    // binding
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        supportActionBar?.apply {
            title = getString(R.string.setting)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.btnAbout.setOnClickListener {
            startActivity(
                Intent(this, AboutActivity::class.java)
            )
        }

        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}