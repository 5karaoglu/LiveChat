package com.besirkaraoglu.livechat.core.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import timber.log.Timber

class BaseFragment: Fragment {

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    var baseContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag("LifeCycle").v("${name()} onViewCreated")
    }

    private fun name(): String {
        return this.javaClass.simpleName
    }
}