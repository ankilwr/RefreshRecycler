package com.mellivora.multiple.status

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.mellivora.multiple.MultipleStatusView
import com.mellivora.pullview.R
import com.mellivora.multiple.api.MultipleStatus
import kotlinx.android.synthetic.main.no_network_view.view.*

class NetErrorStatusView :  MultipleStatus {

    override fun getView(context: Context, statusView: MultipleStatusView): View {
        return LayoutInflater.from(context).inflate(R.layout.no_network_view, statusView, false)
    }

    override fun getRetryView(view: View): View? {
        return view.blockContent
    }

    override fun showMessage(view: View, message: String) {
        view.tvNetworkErrorHint.text = message
    }

}
