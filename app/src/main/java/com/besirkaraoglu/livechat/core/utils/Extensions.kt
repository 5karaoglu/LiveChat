package com.besirkaraoglu.livechat.core.utils

import android.widget.Toast
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

fun Fragment.showToastShort(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()