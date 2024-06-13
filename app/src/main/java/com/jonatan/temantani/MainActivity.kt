package com.jonatan.temantani

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jonatan.temantani.databinding.ActivityMainBinding
import com.jonatan.temantani.view.analisis.AnalyzeActivity
import com.jonatan.temantani.view.calculate.CalculateActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutCalculate.setOnClickListener(){
            val intent = Intent(this, CalculateActivity::class.java)
            startActivity(intent)
        }
        binding.layoutDetect.setOnClickListener(){
            val intent = Intent(this, AnalyzeActivity::class.java)
            startActivity(intent)
        }
    }
}