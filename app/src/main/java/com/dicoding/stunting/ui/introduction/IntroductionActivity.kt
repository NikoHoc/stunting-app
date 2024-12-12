package com.dicoding.stunting.ui.introduction

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.dicoding.stunting.R
import com.dicoding.stunting.databinding.ActivityAuthenticationBinding
import com.dicoding.stunting.databinding.ActivityIntroductionBinding
import com.dicoding.stunting.ui.authentication.login.LoginFragment
import com.dicoding.stunting.ui.introduction.material.DefinitionFragment

class IntroductionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroductionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        window.statusBarColor = resources.getColor(R.color.mint_green, theme)
        supportActionBar?.hide()

        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.introduction_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val fragmentManager = supportFragmentManager
        val definitionFragment = DefinitionFragment()
        val fragment = fragmentManager.findFragmentByTag(DefinitionFragment::class.java.simpleName)

        if (fragment !is DefinitionFragment) {
            fragmentManager.commit {
                add(R.id.introduction_activity, definitionFragment, DefinitionFragment::class.java.simpleName)
            }
        }
    }
}