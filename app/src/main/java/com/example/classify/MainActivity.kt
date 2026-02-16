package com.example.classify

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var ivPreview: ImageView
    private lateinit var tvPlaceholder: TextView
    private lateinit var tvPrediction: TextView
    private lateinit var tvConfidence: TextView
    private lateinit var cardResult: CardView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnCamera: Button
    private lateinit var btnGallery: Button

    private lateinit var classifier: SkinLesionClassifier
    private var currentPhotoPath: String = ""

    companion object {
        private const val CAMERA_PERMISSION_CODE = 100
    }

    // Camera launcher
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            displayImage(bitmap)
            classifyImage(bitmap)
        }
    }

    // Gallery launcher
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            try {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(contentResolver, it)
                }
                displayImage(bitmap)
                classifyImage(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupClassifier()
        setupClickListeners()
    }

    private fun initializeViews() {
        ivPreview = findViewById(R.id.ivPreview)
        tvPlaceholder = findViewById(R.id.tvPlaceholder)
        tvPrediction = findViewById(R.id.tvPrediction)
        tvConfidence = findViewById(R.id.tvConfidence)
        cardResult = findViewById(R.id.cardResult)
        progressBar = findViewById(R.id.progressBar)
        btnCamera = findViewById(R.id.btnCamera)
        btnGallery = findViewById(R.id.btnGallery)
    }

    private fun setupClassifier() {
        try {
            classifier = SkinLesionClassifier(this)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to initialize classifier: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupClickListeners() {
        btnCamera.setOnClickListener {
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }

        btnGallery.setOnClickListener {
            openGallery()
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        try {
            val photoFile = createImageFile()
            val photoUri = FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.fileprovider",
                photoFile
            )
            currentPhotoPath = photoFile.absolutePath
            takePicture.launch(photoUri)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to create image file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(): File {
        val timeStamp = System.currentTimeMillis()
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = getExternalFilesDir(null)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private fun openGallery() {
        pickImage.launch("image/*")
    }

    private fun displayImage(bitmap: Bitmap) {
        ivPreview.setImageBitmap(bitmap)
        tvPlaceholder.visibility = View.GONE
        cardResult.visibility = View.GONE
    }

    private fun classifyImage(bitmap: Bitmap) {
        progressBar.visibility = View.VISIBLE
        cardResult.visibility = View.GONE

        // Run classification in background
        Thread {
            try {
                val result = classifier.classify(bitmap)

                runOnUiThread {
                    displayResult(result)
                    progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Classification failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        }.start()
    }

    private fun displayResult(result: ClassificationResult) {
        cardResult.visibility = View.VISIBLE

        tvPrediction.text = result.label
        tvConfidence.text = "Confidence: ${String.format("%.1f", result.confidence * 100)}%"

        // Set color based on prediction
        if (result.label.contains("Benign", ignoreCase = true)) {
            tvPrediction.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
        } else {
            tvPrediction.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        classifier.close()
    }
}