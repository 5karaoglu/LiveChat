package com.besirkaraoglu.livechat.core.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(resId: Int){
    findNavController().navigate(resId)
}

fun LifecycleOwner.toScreenName(): String {
    val ownerName = this.toString()
    if (ownerName.contains("Fragment")) {
        return ownerName.substring(0, ownerName.indexOf("{"))
    }
    return ownerName
}