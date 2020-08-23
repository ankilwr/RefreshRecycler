package com.mellivora.demo.refresh

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.mellivora.demo.R
import com.mellivora.multiple.MultipleStatusView
import com.mellivora.multiple.api.MultipleStatus
import kotlinx.android.synthetic.main.layout_error.view.*

class DemoErrorStatus: MultipleStatus {

    override fun getView(context: Context, statusView: MultipleStatusView): View {
        return LayoutInflater.from(context).inflate(R.layout.layout_error, statusView, false)
    }

    override fun getRetryView(view: View): View? {
        return view.ivError
    }

    override fun showMessage(view: View, message: String) {
        view.tvError.text = message
    }

}