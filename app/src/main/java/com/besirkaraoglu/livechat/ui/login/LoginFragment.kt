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

        checkIfPendingResult()

        binding.button.setOnClickListener {
            loginWithTwitter()
        }
    }

    private fun checkIfPendingResult() {
        val pendingResultTask: Task<AuthResult>? = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener(
                    OnSuccessListener<AuthResult?> {
                        // User is signed in.
                        // IdP data available in
                        // authResult.getAdditionalUserInfo().getProfile().
                        // The OAuth access token can also be retrieved:
                        // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                        // The OAuth secret can be retrieved by calling:
                        // ((OAuthCredential)authResult.getCredential()).getSecret().
                    })
                .addOnFailureListener(
                    OnFailureListener {
                        // Handle failure.
                    })
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.
        }
    }

    private fun loginWithTwitter() {
        firebaseAuth
            .startActivityForSignInWithProvider( /* activity= */requireActivity(), provider.build())
            .addOnSuccessListener {
                // User is signed in.
                // IdP data available in
                // authResult.getAdditionalUserInfo().getProfile().
                // The OAuth access token can also be retrieved:
                // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                // The OAuth secret can be retrieved by calling:
                // ((OAuthCredential)authResult.getCredential()).getSecret().
                Timber.tag("Twitter").d( "loginWithTwitter: %s", it.additionalUserInfo?.profile)
            }
            .addOnFailureListener {
                showToastShort("Login failed! ${it.message}")
            }
    }

    private fun navigateToMain(){
        navigate(R.id.mainFragment)
    }
}