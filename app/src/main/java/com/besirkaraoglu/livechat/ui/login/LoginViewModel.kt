package com.besirkaraoglu.livechat.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.besirkaraoglu.livechat.core.base.BaseViewModel
import com.besirkaraoglu.livechat.core.utils.Resource
import com.besirkaraoglu.livechat.data.model.Users
import com.besirkaraoglu.livechat.data.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val usersRepository: UsersRepository
) : BaseViewModel() {

    private val _insertResult = MutableLiveData<Resource<Users>>()
    val insertResult get() = _insertResult

    fun insertUser(user: Users) = viewModelScope.launch {
        usersRepository.insertUser(user).collect { result ->
            _insertResult.value = result
        }
    }

}