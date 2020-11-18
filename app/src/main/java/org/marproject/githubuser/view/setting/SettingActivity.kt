package org.marproject.githubuser.view.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import org.marproject.githubuser.R
import org.marproject.githubuser.databinding.ActivitySettingBinding
import org.marproject.githubuser.utils.receiver.AlarmReceiver
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

        val alarmReceiver = AlarmReceiver()
        val preferences = this.getSharedPreferences(getString(R.string.preference_name), Context.MODE_PRIVATE)

        binding.switchAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                preferences.edit().putBoolean(getString(R.string.saved_switch), true).apply()
                alarmReceiver.setRepeatingAlarm(this, getString(R.string.reminder_message))
            } else {
                preferences.edit().putBoolean(getString(R.string.saved_switch), false).apply()
                alarmReceiver.cancelAlarm(this)
            }
        }

        // get preferences
        val switchState = preferences.getBoolean(getString(R.string.saved_switch), false)

        // set checked switch
        binding.switchAlarm.isChecked = switchState

        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}