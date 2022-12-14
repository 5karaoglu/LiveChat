package com.besirkaraoglu.livechat.ui.messages

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.besirkaraoglu.livechat.core.base.BaseViewModel
import com.besirkaraoglu.livechat.core.utils.Resource
import com.besirkaraoglu.livechat.data.model.MessagesItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel
@Inject constructor(): BaseViewModel(){

    private val _messageItems = MutableLiveData<Resource<List<MessagesItem>>>()
    val messageItems get() = _messageItems

    fun getMessages(uid: String) = viewModelScope.launch {
        _messageItems.value = Resource.Success(
            listOf(
                MessagesItem("","Hammond Jr.","","Hey! when is the new album release?","1"),
                MessagesItem("","Nikolai","","I play bass man!","1"),
                MessagesItem("","Fabrizio","","Gimme drums.","1"),
                MessagesItem("","Nick","","I'm cool you know.","1"),
            )
        )
    }
}