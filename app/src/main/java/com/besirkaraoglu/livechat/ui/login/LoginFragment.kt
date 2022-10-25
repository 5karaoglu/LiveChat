package com.besirkaraoglu.livechat.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.besirkaraoglu.livechat.R
import com.besirkaraoglu.livechat.core.PROVIDER_ID_TWITTER
import com.besirkaraoglu.livechat.core.base.BaseFragment
import com.besirkaraoglu.livechat.core.utils.binding.viewBinding
import com.besirkaraoglu.livechat.core.utils.navigate
import com.besirkaraoglu.livechat.core.utils.showToastShort
import com.besirkaraoglu.livechat.databinding.FragmentLoginBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel by viewModels<LoginViewModel>()

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val provider = OAuthProvider.newBuilder(PROVIDER_ID_TWITTER)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initListeners()
    }

    private fun initListeners() {
        binding.buttonLoginWithTwitter.setOnClickListener {
            checkIfPendingResult()
        }
    }

    private fun checkIfPendingResult() {
        val pendingResultTask: Task<AuthResult>? = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener(handleSuccessfulLogin)
                .addOnFailureListener(handleFailedLogin)
        } else {
            loginWithTwitter()
        }
    }

    private fun loginWithTwitter() {
        firebaseAuth
            .startActivityForSignInWithProvider(requireActivity(), provider.build())
            .addOnSuccessListener(handleSuccessfulLogin)
            .addOnFailureListener(handleFailedLogin)
    }

    private val handleSuccessfulLogin = OnSuccessListener<AuthResult> { authResult ->
        Timber.d("TwitterAuth successful.")
        navigateToMain()
    }

    private val handleFailedLogin = OnFailureListener { e ->
        showToastShort("Login failed! ${e.message}")
    }

    private fun navigateToMain(){
        navigate(R.id.mainFragment)
    }
}