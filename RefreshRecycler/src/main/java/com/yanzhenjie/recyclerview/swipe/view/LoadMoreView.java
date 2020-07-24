package com.yanzhenjie.recyclerview.swipe.view;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

public class LoadMoreView extends LinearLayout implements SwipeMenuRecyclerView.LoadMoreAction {

    private TextView textview;


    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_load_more, this);
        textview = findViewById(R.id.textHint);
    }

    @Override
    public void onLoading() {
        setClickable(false);
        textview.setText("正在加载...");
    }

    @Override
    public void onLoadCancel() {
        setClickable(false);
        textview.setText("查看更多");
    }

    @Override
    public void onLoadFinish() {
        setClickable(true);
        textview.setText("查看更多");
    }

    @Override
    public void onLoadError(int errorCode, String errorMessage) {
        setClickable(true);
        textview.setText("查看更多");
    }
}
