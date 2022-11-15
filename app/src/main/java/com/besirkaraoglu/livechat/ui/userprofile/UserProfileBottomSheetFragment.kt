package com.besirkaraoglu.livechat.ui.userprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.besirkaraoglu.livechat.R
import com.besirkaraoglu.livechat.core.utils.binding.viewBinding
import com.besirkaraoglu.livechat.core.utils.showToastShort
import com.besirkaraoglu.livechat.databinding.FragmentUserProfileBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class UserProfileBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(FragmentUserProfileBottomSheetBinding::bind)
    private val viewModel by viewModels<UserProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_profile_bottom_sheet,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSendMessage.setOnClickListener {
            showToastShort("sup")
        }
    }


}