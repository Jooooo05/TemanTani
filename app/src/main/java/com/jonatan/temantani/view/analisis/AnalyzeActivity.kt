package com.jonatan.temantani.view.analisis

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jonatan.temantani.R
import com.jonatan.temantani.databinding.ActivityAnalyzeBinding
import com.yalantis.ucrop.UCrop
import java.io.File

class AnalyzeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalyzeBinding

    //variabel for image
    private var currentImageUri: Uri? = null
    private var croppedImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnOpenGallery.setOnClickListener { startGallery() }
        binding.btnAnalyze.setOnClickListener {
            currentImageUri?.let {
                analyzeImage()
                moveToResult()
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            Log.d("AnalyzeActivity", "Hasil currentImageUri : $currentImageUri")
            showImage()
            startUCrop(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startUCrop(sourceUri: Uri) {
        val fileName = "cropped_image_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(cacheDir, fileName))

        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)
            .start(this)
    }

    // Handle UCrop activity result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                cropImage(resultUri)
            } ?: showToast("Failed to crop image")
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            showToast("Crop error: ${cropError?.message}")
        }
    }

    private fun cropImage(uri: Uri) {
        binding.imgResult.setImageURI(uri)
        croppedImage = uri
        Log.d("MainActivity", "Ngecek Output Dari croppeImage = $croppedImage")
    }

    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgResult.setImageURI(it)
        } ?:  Log.d("Image URI", "No image to display")
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        val intent = Intent(this, ResultActivity::class.java)
        croppedImage?.let { uri ->
            intent.putExtra(ResultActivity.JO_IMAGE_URI, uri.toString())
            startActivity(intent)
        } ?: showToast(getString(R.string.image_classifier_failed))
    }

    private fun moveToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        croppedImage?.let { uri ->
            intent.putExtra(ResultActivity.JO_IMAGE_URI, uri.toString())
            startActivity(intent)
        } ?: showToast(getString(R.string.image_classifier_failed))
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}