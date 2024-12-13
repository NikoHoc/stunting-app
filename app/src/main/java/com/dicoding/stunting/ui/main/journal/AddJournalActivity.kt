package com.dicoding.stunting.ui.main.journal

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.ActivityAddJournalBinding
import com.dicoding.stunting.ui.ViewModelFactory
import com.dicoding.stunting.data.remote.Result
import com.dicoding.stunting.ui.main.MainActivity
import com.dicoding.stunting.utils.getImageUri
import com.dicoding.stunting.utils.reduceFileImage
import com.dicoding.stunting.utils.uriToFile

class AddJournalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddJournalBinding

    private val journalViewModel  by viewModels<JournalViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, resources.getString(R.string.permission_granted), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, resources.getString(R.string.permission_denied), Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.leaf_green, theme)
        supportActionBar?.hide()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkFieldsForErrors()

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        setupAction()
    }

    private fun setupAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.edDescription.addTextChangedListener(inputListener())
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }

        binding.btnUpload.setOnClickListener { uploadJournal() }

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivJournal.setImageURI(it)
        }
    }

    @SuppressLint("NewApi")
    private fun uploadJournal() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this@AddJournalActivity).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.edDescription.text.toString()
            Log.d("Story Description", "description: $description")

            journalViewModel.uploadJournal(imageFile, description).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            val uploadResponse = result.data
                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.success_upload_alert)
                                setMessage(uploadResponse.message)
                                setPositiveButton(R.string.success_alert_reply) { _, _ ->
                                    finish()
                                }
                                create()
                                show()
                            }
                            binding.progressIndicator.visibility = View.GONE
                        }
                        is Result.Error -> {
                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.failed_upload_alert)
                                setMessage(result.error)
                                setPositiveButton(R.string.error_alert_reply) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                create()
                                show()
                            }
                            binding.progressIndicator.visibility = View.GONE
                        }
                    }
                }
            }
        } ?: Toast.makeText(this, getString(R.string.image_error), Toast.LENGTH_LONG).show()
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
        val descriptionError = binding.edDescription.error != null || binding.edDescription.text.isNullOrBlank()

        val isEnabled = !descriptionError
        binding.btnUpload.isEnabled = isEnabled

        if (isEnabled) {
            binding.btnUpload.setBackgroundResource(R.drawable.rounded_corner)
        } else {
            binding.btnUpload.setBackgroundResource(R.drawable.rounded_disabled_button)
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}