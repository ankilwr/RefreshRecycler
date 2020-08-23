package com.mellivora.multiple.api;


import android.content.Context;

import com.mellivora.multiple.MultipleStatusView;

/**
 * 网络错误布局构建器
 */
public interface StatusViewCreator {
    MultipleStatus createStatusView(Context context, MultipleStatusView layout);
}