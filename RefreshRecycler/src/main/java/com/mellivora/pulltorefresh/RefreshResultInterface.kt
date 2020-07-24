package com.mellivora.pulltorefresh

import com.mellivora.error.PullErrorStatus


interface RefreshResultInterface {

    var page: Int

    fun loadError(isRefresh: Boolean, message: String? = "加载失败", code: Int = PullErrorStatus.UNKNOWN_ERROR)

    fun loadFinish(isRefresh: Boolean, hasMore: Boolean, footerDivider: Boolean = true){
        if (isRefresh) {
            page = if (hasMore) 2 else 1
        } else {
            if (hasMore) page += 1
        }
    }

}