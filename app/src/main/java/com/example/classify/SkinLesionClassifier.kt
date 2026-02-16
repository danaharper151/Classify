package com.example.classify

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class ClassificationResult(
    val label: String,
    val confidence: Float
)

class SkinLesionClassifier(private val context: Context) {

    private var interpreter: Interpreter? = null
    private val inputSize = 224

    // For binary classification
    private val labels = arrayOf("Benign", "Malignant")

    init {
        try {
            val model = FileUtil.loadMappedFile(context, "skin_lesion_model.tflite")
            val options = Interpreter.Options().apply {
                setNumThreads(4)
            }
            interpreter = Interpreter(model, options)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Failed to load model: ${e.message}")
        }
    }

    fun classify(bitmap: Bitmap): ClassificationResult {
        val interpreter = interpreter ?: throw IllegalStateException("Interpreter not initialized")

        // Preprocess image
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
        val inputBuffer = convertBitmapToByteBuffer(resizedBitmap)

        // Prepare output buffer
        val outputShape = interpreter.getOutputTensor(0).shape()
        val output = Array(1) { FloatArray(outputShape[1]) }

        // Run inference
        interpreter.run(inputBuffer, output)

        // Process results
        val predictions = output[0]

        // For binary classification
        return if (predictions.size == 1) {
            // Single output (sigmoid)
            val confidence = predictions[0]
            if (confidence > 0.5) {
                ClassificationResult("Malignant", confidence)
            } else {
                ClassificationResult("Benign", 1 - confidence)
            }
        } else {
            // Two outputs (softmax)
            val malignantConfidence = predictions[1]
            if (malignantConfidence > 0.5) {
                ClassificationResult("Malignant", malignantConfidence)
            } else {
                ClassificationResult("Benign", predictions[0])
            }
        }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        // Ensure bitmap is mutable and in ARGB_8888 format
        val mutableBitmap = if (bitmap.config != Bitmap.Config.ARGB_8888 || !bitmap.isMutable) {
            bitmap.copy(Bitmap.Config.ARGB_8888, true)
        } else {
            bitmap
        }

        val pixels = IntArray(inputSize * inputSize)
        try {
            mutableBitmap.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize)
        } catch (e: Exception) {
            // If getPixels fails, manually iterate through pixels
            for (y in 0 until inputSize) {
                for (x in 0 until inputSize) {
                    pixels[y * inputSize + x] = mutableBitmap.getPixel(x, y)
                }
            }
        }

        var pixel = 0
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val value = pixels[pixel++]

                // Extract RGB values and normalize to [0, 1]
                val r = ((value shr 16) and 0xFF) / 255.0f
                val g = ((value shr 8) and 0xFF) / 255.0f
                val b = (value and 0xFF) / 255.0f

                // Add to buffer
                byteBuffer.putFloat(r)
                byteBuffer.putFloat(g)
                byteBuffer.putFloat(b)
            }
        }

        return byteBuffer
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }
}