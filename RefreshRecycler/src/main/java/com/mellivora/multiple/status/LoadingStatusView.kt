package com.mellivora.multiple.status

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.mellivora.multiple.MultipleStatusView
import com.mellivora.pullview.R
import com.mellivora.multiple.api.MultipleStatus
import kotlinx.android.synthetic.main.loading_view.view.*

class LoadingStatusView: MultipleStatus {

    override fun getView(context: Context, statusView: MultipleStatusView): View {
        return LayoutInflater.from(context).inflate(R.layout.loading_view, statusView, false)
    }

    override fun getRetryView(view: View): View? {
        return null
    }

    override fun showMessage(view: View, message: String) {
        view.tvLoading.text = message
    }


}
