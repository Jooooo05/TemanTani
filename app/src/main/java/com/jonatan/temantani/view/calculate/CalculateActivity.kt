package com.jonatan.temantani.view.calculate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jonatan.temantani.R
import com.jonatan.temantani.databinding.ActivityCalculateBinding

class CalculateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalculate.setOnClickListener(){
            val intent = Intent(this, ResultCalculateActivity::class.java)
            startActivity(intent)
        }

    }
}