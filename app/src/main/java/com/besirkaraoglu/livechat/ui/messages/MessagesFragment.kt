package com.besirkaraoglu.livechat.ui.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.besirkaraoglu.livechat.R
import com.besirkaraoglu.livechat.core.base.BaseFragment
import com.besirkaraoglu.livechat.core.utils.Resource
import com.besirkaraoglu.livechat.core.utils.binding.viewBinding
import com.besirkaraoglu.livechat.core.utils.loadImageWithGlide
import com.besirkaraoglu.livechat.data.model.MessagesItem
import com.besirkaraoglu.livechat.databinding.FragmentMessagesBinding
import com.besirkaraoglu.livechat.databinding.LayoutMessagesItemBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimyilmaz.kiel.adapterOf
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

@AndroidEntryPoint
class MessagesFragment : BaseFragment(R.layout.fragment_messages) {

    private val binding by viewBinding(FragmentMessagesBinding::bind)
    private val viewModel by viewModels<MessagesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        getMessages()
    }

    private fun getMessages() {
        /*viewModel.getMessages(FirebaseAuth.getInstance().currentUser!!.uid)
        viewModel.messageItems.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Empty -> {}
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    adapter.submitList(result.data)
                }
            }
        }*/
    }

    private fun initRecyclerView() {
        /*binding.rv.adapter = adapter*/
    }

    private val adapter = adapterOf<MessagesItem> {
        register(
            layoutResource = R.layout.layout_messages_item,
            viewHolder = ::MessagesItemViewHolder,
            onViewHolderCreated = { vh ->
                vh.itemView.setOnClickListener {

                }
            },
            onBindViewHolder = { vh, _, item ->
                with(vh.binding) {
                    tvName.text = item.name
                    tvMessage.text = item.message
                    iv.loadImageWithGlide(item.photoUrl!!)
                }
            }
        )
    }
}

class MessagesItemViewHolder(view: View) : RecyclerViewHolder<MessagesItem>(view) {
    val binding = LayoutMessagesItemBinding.bind(view)
}