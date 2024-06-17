package com.jonatan.temantani.view.analisis

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ImageClassifierHelper(
    private var threshold: Float = 0.1f,
    private var maxResults: Int = 3,
    private val modelName: String = "converted_model_mobileNetV2.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {

    private var interpreter: Interpreter? = null
    private val imageSize = 224
    private val imageMean = 127.5f
    private val imageStd = 127.5f

    init {
        setupInterpreter()
    }

    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    // Setup the TensorFlow Lite Interpreter
    private fun setupInterpreter() {
        val options = Interpreter.Options()
        options.setNumThreads(4)
        interpreter = Interpreter(loadModelFile(), options)
    }

    // Classify a static image
    fun classifyStaticImage(imageUri: Uri) {
        if (interpreter == null) {
            setupInterpreter()
        }

        val bitmap = toBitmap(imageUri)
        val tensorImage = imageProcessor(bitmap)

        val inputBuffer = tensorImage.buffer
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 1001), DataType.FLOAT32).buffer

        val inferenceTime = SystemClock.uptimeMillis()
        interpreter?.run(inputBuffer, outputBuffer)
        val elapsedTime = SystemClock.uptimeMillis() - inferenceTime

        val results = getClassificationResults(outputBuffer, threshold = 0.5, topK = 3)
        classifierListener?.onResults(results, elapsedTime)
    }

    // Convert Uri to Bitmap
    private fun toBitmap(image: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, image)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, image)
        }.copy(Bitmap.Config.ARGB_8888, true)
    }

    // Process the image
    private fun imageProcessor(bitmap: Bitmap): TensorImage {
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(imageSize, imageSize, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(imageMean, imageStd))
            .build()

        return imageProcessor.process(tensorImage)
    }

    // Get image classification results
//    private fun getClassificationResults(outputBuffer: ByteBuffer): List<Classifications> {
//        outputBuffer.rewind()
//        val probabilities = FloatArray(1001)
//        outputBuffer.asFloatBuffer().get(probabilities)
//
//        val categories = mutableListOf<Category>()
//        probabilities.forEachIndexed { index, score ->
//            categories.add(Category(index.toString(), score))
//        }
//
//        categories.sortByDescending { it.score }
//        return listOf(Classifications(categories))
//    }

    // Map indeks kelas ke nama penyakit
    val classMap = mapOf(
        0 to "Padi sehat",
        1 to "Penyakit Bacterialblight",
        2 to "Penyakit Blast",
        3 to "Penyakit Brownspot",
        4 to "Penyakit Tungro"
    )
//    private fun getClassificationResults(outputBuffer: ByteBuffer): List<Classifications> {
//        outputBuffer.rewind()
//        val probabilities = FloatArray(1001)
//        outputBuffer.asFloatBuffer().get(probabilities)
//
//        val categories = mutableListOf<Category>()
//        probabilities.forEachIndexed { index, score ->
//            val className = classMap[index] ?: "Unknown"
//            categories.add(Category(className, score))
//        }
//
//        categories.sortByDescending { it.score }
//        return listOf(Classifications(categories))
//    }
//


    private fun getClassificationResults(outputBuffer: ByteBuffer, threshold: Double, topK: Int): List<Classifications> {
        outputBuffer.rewind()
        val probabilities = FloatArray(1001)
        outputBuffer.asFloatBuffer().get(probabilities)

        val filteredProbabilities = mutableListOf<Float>()
        probabilities.forEachIndexed { index, score ->
            if (score >= threshold) {
                filteredProbabilities.add(score)
            }
        }

        val topKIndices = filteredProbabilities
            .mapIndexed { index, score -> Pair(index, score) }
            .sortedByDescending { it.second }
            .take(topK)
            .map { it.first }

        val categories = mutableListOf<Category>()
        topKIndices.forEach { index ->
            val className = classMap[index] ?: "Unknown"
            categories.add(Category(className, probabilities[index]))
        }

        return listOf(Classifications(categories))
    }




    data class Category(
        val label: String,
        val score: Float
    )

    data class Classifications(
        val categories: List<Category>
    )

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?,
            inferenceTime: Long
        )
    }
}
