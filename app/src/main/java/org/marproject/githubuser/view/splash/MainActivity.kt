package org.marproject.githubuser.view.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.marproject.githubuser.databinding.ActivityMainBinding
import org.marproject.githubuser.view.main.HomeActivity

class MainActivity : AppCompatActivity() {

    // binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val background = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()

        // set content view
        setContentView(binding.root)
    }

}