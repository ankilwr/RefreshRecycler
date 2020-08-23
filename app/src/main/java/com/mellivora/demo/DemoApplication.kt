package com.mellivora.demo

import android.app.Application
import android.content.Context
import com.mellivora.demo.refresh.DemoErrorStatus
import com.mellivora.multiple.MultipleStatusView
import com.mellivora.multiple.api.MultipleStatus
import com.mellivora.multiple.api.StatusViewCreator
import com.mellivora.multiple.status.EmptyStatusView
import com.mellivora.multiple.status.ErrorStatusView
import com.mellivora.multiple.status.LoadingStatusView
import com.mellivora.multiple.status.NetErrorStatusView
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout


class DemoApplication : Application(){

    override fun onCreate() {
        super.onCreate()
    }

    init {
        //设置刷新布局的全局属性
        //设置全局的刷新Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ -> ClassicsHeader(context) }
        //设置全局的加载更多Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> ClassicsFooter(context) }

        //设置多状态布局的全局属性
        MultipleStatusView.setLoadingCreator{ _, _ -> LoadingStatusView() }
        MultipleStatusView.setEmptyCreator{ _, _ -> EmptyStatusView() }
        MultipleStatusView.setErrorCreator{ _, _ -> ErrorStatusView() }
        MultipleStatusView.setNetErrorCreator{ _, _ -> NetErrorStatusView() }
    }

}