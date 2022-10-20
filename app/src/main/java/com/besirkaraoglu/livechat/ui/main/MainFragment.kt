package com.besirkaraoglu.livechat.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.besirkaraoglu.livechat.R
import com.besirkaraoglu.livechat.core.base.BaseFragment
import com.besirkaraoglu.livechat.databinding.FragmentMainBinding
import com.besirkaraoglu.livechat.utils.binding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel by viewModels<MainViewModel>()



}