package com.lustia.story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lustia.story.databinding.ActivityMainBinding
import com.lustia.story.ui.GameFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, GameFragment())
                .commit()
        }
    }
}
