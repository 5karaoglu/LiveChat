package com.besirkaraoglu.livechat.ui.messagescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.besirkaraoglu.livechat.R
import com.besirkaraoglu.livechat.core.base.BaseFragment
import com.besirkaraoglu.livechat.core.utils.binding.viewBinding
import com.besirkaraoglu.livechat.databinding.FragmentDirectMessageBinding
import com.besirkaraoglu.livechat.databinding.FragmentMessagesBinding
import com.besirkaraoglu.livechat.ui.messages.MessagesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DirectMessageFragment : BaseFragment(R.layout.fragment_direct_message) {

    private val binding by viewBinding(FragmentDirectMessageBinding::bind)
    private val viewModel by viewModels<DirectMessageViewModel>()

}