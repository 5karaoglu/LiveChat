package com.besirkaraoglu.livechat.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.besirkaraoglu.livechat.core.utils.ScreenAnalyticsHandler
import com.besirkaraoglu.livechat.core.utils.ScreenAnalyticsHandlerImpl
import timber.log.Timber

open class BaseFragment(contentLayoutId: Int): Fragment(contentLayoutId),
    ScreenAnalyticsHandler by ScreenAnalyticsHandlerImpl() {


    var baseContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerLifecycleOwner(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag("Lifecycle").v("${name()} onViewCreated")
    }

    private fun name(): String {
        return this.javaClass.simpleName
    }
}