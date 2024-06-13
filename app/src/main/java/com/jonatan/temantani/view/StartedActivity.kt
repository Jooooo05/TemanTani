package com.jonatan.temantani.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jonatan.temantani.R
import com.jonatan.temantani.databinding.ActivityStartedBinding
import com.jonatan.temantani.view.welcome.WelcomeActivity

class StartedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStarted.setOnClickListener(){
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }
    }
}