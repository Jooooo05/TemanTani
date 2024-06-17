package com.jonatan.temantani.view.analisis

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.jonatan.temantani.R
import com.jonatan.temantani.databinding.ActivityResultBinding
import com.jonatan.temantani.view.analisis.ImageClassifierHelper.Classifications

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.getStringExtra(JO_IMAGE_URI)
        Log.d("ResultActivity", "Mengecek kiriman data dari MainActivity hasil imageUri : $imageUri")
        if (imageUri != null) {
            val image = Uri.parse(imageUri)
            Log.d("ResultActivity", "Ngecek isi variable image : $image" )
            displayImage(image)
            classifyImage(image)
        } else {
            showError("No image URI provided")
            finish()
        }
    }

    private fun classifyImage(image: Uri) {
        val imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(errorMessage: String) {
                    showError(errorMessage)
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    results?.let { showResults(it) }
                }
            }
        )
        imageClassifierHelper.classifyStaticImage(image)
    }

    private fun showResults(results: List<Classifications>) {
        val topResult = results[0]
        val label = topResult.categories[0].label
        val score = topResult.categories[0].score

        fun Float.formatToString(): String {
            return String.format("%.2f%%", this * 100)
        }

        binding.resultText.text = "$label ${score.formatToString()}"
    }

    private fun displayImage(uri: Uri) {
        binding.resultImage.setImageURI(uri)
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val JO_IMAGE_URI = "tempat_image_uri"
    }
}
