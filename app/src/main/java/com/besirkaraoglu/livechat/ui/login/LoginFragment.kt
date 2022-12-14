package com.besirkaraoglu.livechat.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.besirkaraoglu.livechat.R
import com.besirkaraoglu.livechat.core.BANNER_URL_KEY_TWITTER
import com.besirkaraoglu.livechat.core.DESCRIPTION_KEY_TWITTER
import com.besirkaraoglu.livechat.core.ID_KEY_TWITTER
import com.besirkaraoglu.livechat.core.PROVIDER_ID_TWITTER
import com.besirkaraoglu.livechat.core.base.BaseFragment
import com.besirkaraoglu.livechat.core.utils.Resource
import com.besirkaraoglu.livechat.core.utils.binding.viewBinding
import com.besirkaraoglu.livechat.core.utils.navigate
import com.besirkaraoglu.livechat.core.utils.showToastShort
import com.besirkaraoglu.livechat.data.model.Users
import com.besirkaraoglu.livechat.databinding.FragmentLoginBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
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
        //temporary button to pass login process
        binding.buttonPassLogin.setOnClickListener {
            navigateToMain()
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

    private fun insertUser(user: Users){
        viewModel.insertUser(user)
        viewModel.insertResult.observe(viewLifecycleOwner){ result ->
            when(result){
                is Resource.Error -> {
                    Timber.e(result.message)
                }
                is Resource.Loading -> {
                    Timber.d("Insert started.")
                }
                is Resource.Success -> {
                    Timber.d("Insert Succeed.")
                    navigateToMain()
                }
                is Resource.Empty -> {}
            }
        }
    }

    private val handleSuccessfulLogin = OnSuccessListener<AuthResult> { authResult ->
        Timber.d("TwitterAuth successful.")
        val username = authResult.additionalUserInfo?.username
        val bio = authResult.additionalUserInfo?.profile?.get(DESCRIPTION_KEY_TWITTER).toString()
        val bannerUrl = authResult.additionalUserInfo?.profile?.get(BANNER_URL_KEY_TWITTER).toString()
        val twitterId = authResult.additionalUserInfo?.profile?.get(ID_KEY_TWITTER).toString()
        with(authResult.user!!){
            insertUser(Users(uid,username,displayName,
                photoUrl.toString(),bio,twitterId, bannerUrl))
        }
    }

    private val handleFailedLogin = OnFailureListener { e ->
        showToastShort("Login failed! ${e.message}")
    }

    private fun navigateToMain(){
        navigate(R.id.mainFragment)
    }
}