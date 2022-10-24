package com.besirkaraoglu.livechat.ui.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.besirkaraoglu.livechat.R
import com.besirkaraoglu.livechat.core.base.BaseFragment
import com.besirkaraoglu.livechat.core.utils.binding.viewBinding
import com.besirkaraoglu.livechat.databinding.FragmentMessagesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagesFragment : BaseFragment(R.layout.fragment_messages) {

    private val binding by viewBinding(FragmentMessagesBinding::bind)
    private val viewModel by viewModels<MessagesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}