package com.mellivora.multiple.api

import android.content.Context
import android.view.View
import com.mellivora.multiple.MultipleStatusView


/**
 * View的状态切换
 */
interface MultipleStatus {

    /**
     * 获取当前视图
     */
    fun getView(context: Context, statusView: MultipleStatusView): View

    /**
     * 点击重新加载的按钮
     * @param view: 对应状态View
     */
    fun getRetryView(view: View): View?

    /**
     * 显示提示信息
     * @param view: 对应状态View
     * @param message: 要显示的内容
     */
    fun showMessage(view: View, message: String)


}