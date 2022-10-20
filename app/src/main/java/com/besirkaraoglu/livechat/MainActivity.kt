package com.besirkaraoglu.livechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.besirkaraoglu.livechat.databinding.ActivityMainBinding
import com.besirkaraoglu.livechat.utils.binding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}