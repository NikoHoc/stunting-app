package com.dicoding.stunting.ui.helper

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.dicoding.stunting.R
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tflite.client.TfLiteInitializationOptions
import com.google.android.gms.tflite.gpu.support.TfLiteGpu
import com.google.android.gms.tflite.java.TfLite
import org.tensorflow.lite.InterpreterApi
import org.tensorflow.lite.gpu.GpuDelegateFactory
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class StuntingHelper (
    private val modelName: String = "StuntingClassificationModel.tflite",
    val context: Context,
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit,
) {
    private var isGPUSupported: Boolean = false

    private var interpreter: InterpreterApi? = null

    init {
        Log.d(TAG, "Checking GPU delegate availability...")
        TfLiteGpu.isGpuDelegateAvailable(context).onSuccessTask { gpuAvailable ->
            Log.d(TAG, "GPU Delegate Availability: $gpuAvailable")
            val optionsBuilder = TfLiteInitializationOptions.builder()
            if (gpuAvailable) {
                optionsBuilder.setEnableGpuDelegateSupport(true)
                isGPUSupported = true
                Log.d(TAG, "GPU delegate support enabled.")
            } else {
                Log.d(TAG, "GPU delegate not available.")
            }
            TfLite.initialize(context, optionsBuilder.build())
        }.addOnSuccessListener {
            Log.d(TAG, "TensorFlow Lite initialized successfully.")
            loadLocalModel()
        }.addOnFailureListener { e ->
            Log.e(TAG, "TensorFlow Lite initialization failed: ${e.message}")
            onError(context.getString(R.string.tflite_is_not_initialized_yet))
        }
    }

    private fun loadLocalModel() {
        try {
            Log.d(TAG, "Loading TFLite model...")
            val buffer: ByteBuffer = loadModelFile(context.assets, modelName)
            initializeInterpreter(buffer)
            Log.d(TAG, "Model loaded successfully!")
        } catch (ioException: IOException) {
            Log.e(TAG, "Error loading model file: ${ioException.message}")
            onError(context.getString(R.string.tflite_is_not_initialized_yet))
        }
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        Log.d(TAG, "Loading model file from assets: $modelPath")
        assetManager.openFd(modelPath).use { fileDescriptor ->
            FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
                val fileChannel = inputStream.channel
                val startOffset = fileDescriptor.startOffset
                val declaredLength = fileDescriptor.declaredLength
                return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            }
        }
    }

    private fun initializeInterpreter(model: Any) {
        Log.d(TAG, "Initializing interpreter...")
        interpreter?.close()
        try {
            val options = InterpreterApi.Options()
                .setRuntime(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)
            if (isGPUSupported) {
                options.addDelegateFactory(GpuDelegateFactory())
                Log.d(TAG, "GPU delegate enabled.")
            }
            if (model is ByteBuffer) {
                interpreter = InterpreterApi.create(model, options)
                Log.d(TAG, "Interpreter initialized successfully!")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Interpreter initialization failed: ${e.message}")
            onError(e.message.toString())
        }
    }

    fun close() {
        interpreter?.close()
    }

    fun predict(age: Int?, height: Int?, gender: Int?) {
        if (interpreter == null) {
            onError(context.getString(R.string.no_tflite_interpreter_loaded))
            return
        }

        if (age == null || height == null || gender == null) {
            onError(context.getString(R.string.invalid_input))
            Log.e(TAG, "Predict failed: Invalid input provided.")
            return
        }

        val inputArray = floatArrayOf(age.toFloat(), gender.toFloat(), height.toFloat())
        Log.d(TAG, "Input data: ${inputArray.joinToString()}")

        val outputArray = Array(1) {
            FloatArray(4)
        }
        try {
            interpreter?.run(inputArray, outputArray)
            // get the prediction output
            val predictions = outputArray[0]
            Log.d(TAG, "Output data: ${predictions.joinToString()}")

            // get the highest value within predictions
            val resultIndex = predictions.indices.maxByOrNull { predictions[it] } ?: -1
            Log.d("StuntingClassificationResult:", "Result index: $resultIndex")
            onResult(
                when (resultIndex) {
                    0 -> context.getString(R.string.severely_stunted)
                    1 -> context.getString(R.string.stunted)
                    2 -> context.getString(R.string.normal)
                    3 -> context.getString(R.string.high)
                    else -> context.getString(R.string.unknown)
                }
            )
        } catch (e: Exception) {
            onError(context.getString(R.string.no_tflite_interpreter_loaded))
            Log.e(TAG, e.message.toString())
        }
    }

    companion object {
        private const val TAG = "StuntingHelper"
    }
}