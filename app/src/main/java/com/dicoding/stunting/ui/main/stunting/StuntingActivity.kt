package com.dicoding.stunting.ui.main.stunting

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.ActivityStuntingBinding
import com.dicoding.stunting.ml.StuntingClassificationModel
import com.dicoding.stunting.ui.helper.StuntingHelper
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class StuntingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStuntingBinding

    private lateinit var stuntingHelper: StuntingHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStuntingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.leaf_green, theme)
        supportActionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()
        checkFieldsForErrors()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        val items = listOf(getString(R.string.male), getString(R.string.female))
        val adapter = ArrayAdapter(this, R.layout.list_item_gender, items)
        (binding.genderInput as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.edAgeInput.addTextChangedListener(inputListener())
        binding.edHeightInput.addTextChangedListener(inputListener())
        binding.genderInput.addTextChangedListener(inputListener())

//        stuntingHelper = StuntingHelper(
//            context = this,
//            onResult = { result ->
//                binding.tvResult.text = result
//            },
//            onError = {errorMessage ->
//                Toast.makeText(this@StuntingActivity, errorMessage, Toast.LENGTH_SHORT).show()
//            }
//        )

        binding.btnAnalyze.setOnClickListener {
            val age = binding.edAgeInput.text.toString().toIntOrNull()
            val height = binding.edHeightInput.text.toString().toIntOrNull()
            val genderValue = when (binding.genderInput.text.toString()) {
                getString(R.string.male) -> 0
                getString(R.string.female) -> 1
                else -> null
            }

            if (age != null && height != null && genderValue != null) {
                // Convert inputs to a ByteBuffer
                val byteBuffer = ByteBuffer.allocateDirect(3 * 4) // 3 integers, each 4 bytes (FLOAT32)
                byteBuffer.order(ByteOrder.nativeOrder())
                byteBuffer.putFloat(age.toFloat())
                byteBuffer.putFloat(genderValue.toFloat())
                byteBuffer.putFloat(height.toFloat())

                // Load and process the model
                val model = StuntingClassificationModel.newInstance(this)
                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 3), DataType.FLOAT32)
                inputFeature0.loadBuffer(byteBuffer)

                // Get the output
                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer
                val outputArray = outputFeature0.floatArray

                // Find the class with the highest probability
                val maxIndex = outputArray.indices.maxByOrNull { outputArray[it] } ?: -1

                // Map the result to the corresponding label
                val result = when (maxIndex) {
                    0 -> getString(R.string.severely_stunted)
                    1 -> getString(R.string.stunted)
                    2 -> getString(R.string.normal)
                    3 -> getString(R.string.high)
                    else -> getString(R.string.unknown)
                }

                // Display the result
                binding.tvResult.text = result

                // Release the model
                model.close()
            } else {
                // Handle invalid input
                binding.tvResult.text = "Please enter valid inputs!"
            }
        }
    }

    private fun inputListener(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkFieldsForErrors()
            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun checkFieldsForErrors() {
        val ageError = binding.edAgeInput.error != null || binding.edAgeInput.text.isNullOrBlank()
        val heightError = binding.edHeightInput.error != null || binding.edHeightInput.text.isNullOrBlank()
        val genderError = binding.genderInput.error != null || binding.genderInput.text.isNullOrBlank()

        val isEnabled = !ageError && !heightError && !genderError
        binding.btnAnalyze.isEnabled = isEnabled

        if (isEnabled) {
            binding.btnAnalyze.setBackgroundResource(R.drawable.rounded_corner)
        } else {
            binding.btnAnalyze.setBackgroundResource(R.drawable.rounded_disabled_button)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stuntingHelper.close()
    }
}