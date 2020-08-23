package com.mellivora.multiple;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.mellivora.multiple.api.MultipleStatus;
import com.mellivora.multiple.api.StatusViewCreator;
import com.mellivora.multiple.status.EmptyStatusView;
import com.mellivora.multiple.status.ErrorStatusView;
import com.mellivora.multiple.status.LoadingStatusView;
import com.mellivora.multiple.status.NetErrorStatusView;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
public class MultipleStatusView extends RelativeLayout {
    private static final String TAG = "MultipleStatusView";

    public static final int STATUS_CONTENT = 0x00;
    public static final int STATUS_LOADING = 0x01;
    public static final int STATUS_EMPTY = 0x02;
    public static final int STATUS_ERROR = 0x03;
    public static final int STATUS_NO_NETWORK = 0x04;


    protected static StatusViewCreator loadingCreator = new StatusViewCreator(){
        @Override
        public MultipleStatus createStatusView(@NotNull Context context, @NotNull MultipleStatusView layout) {
            return new LoadingStatusView();
        }
    };
    protected static StatusViewCreator errorCreator = new StatusViewCreator() {
        @Override
        public MultipleStatus createStatusView(@NotNull Context context, @NotNull MultipleStatusView layout) {
            return new ErrorStatusView();
        }
    };
    protected static StatusViewCreator emptyCreator = new StatusViewCreator() {
        @Override
        public MultipleStatus createStatusView(@NotNull Context context, @NotNull MultipleStatusView layout) {
            return new EmptyStatusView();
        }
    };
    protected static StatusViewCreator netErrorCreator = new StatusViewCreator() {
        @Override
        public MultipleStatus createStatusView(@NotNull Context context, @NotNull MultipleStatusView layout) {
            return new NetErrorStatusView();
        }
    };

    private MultipleStatus mEmptyStatus;
    private MultipleStatus mErrorStatus;
    private MultipleStatus mLoadingStatus;
    private MultipleStatus mNoNetworkStatus;

    private View mEmptyView;
    private View mErrorView;
    private View mLoadingView;
    private View mNoNetworkView;

    private int mViewStatus = -1;
    private OnClickListener mOnRetryClickListener;
    private OnViewStatusChangeListener mViewStatusListener;

    public MultipleStatusView(Context context) {
        this(context, null);
    }

    public MultipleStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLoadingStatus = loadingCreator.createStatusView(context, this);
        mEmptyStatus = emptyCreator.createStatusView(context, this);
        mErrorStatus = errorCreator.createStatusView(context, this);
        mNoNetworkStatus = netErrorCreator.createStatusView(context, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        showContent();
        mViewStatus = -1;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        clear(mEmptyView, mLoadingView, mErrorView, mNoNetworkView);
//        if (!mOtherIds.isEmpty()) {
//            mOtherIds.clear();
//        }
//        if (null != mOnRetryClickListener) {
//            mOnRetryClickListener = null;
//        }
//        if (null != mViewStatusListener) {
//            mViewStatusListener = null;
//        }
    }

    /**
     * 获取当前状态
     *
     * @return 视图状态
     */
    public int getViewStatus() {
        return mViewStatus;
    }

    public boolean isLoading() {
        return mViewStatus == STATUS_LOADING;
    }

    public boolean isSuccess() {
        return mViewStatus == STATUS_CONTENT;
    }


    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    public void setOnRetryClickListener(OnClickListener onRetryClickListener) {
        this.mOnRetryClickListener = onRetryClickListener;
    }

    public static void setLoadingCreator(StatusViewCreator creator){
        loadingCreator = creator;
    }

    public static void setEmptyCreator(StatusViewCreator creator){
        emptyCreator = creator;
    }

    public static void setErrorCreator(StatusViewCreator creator){
        errorCreator = creator;
    }

    public static void setNetErrorCreator(StatusViewCreator creator){
        netErrorCreator = creator;
    }


    public void setLoadingStatus(MultipleStatus status){
        if(mLoadingView != null){
            removeView(mLoadingView);
            mLoadingView = null;
        }
        mLoadingStatus = status;
    }

    public void setEmptyStatus(MultipleStatus status){
        if(mEmptyView != null){
            removeView(mEmptyView);
            mEmptyView = null;
        }
        mEmptyStatus = status;
    }

    public void setErrorStatus(MultipleStatus status){
        if(mErrorView != null){
            removeView(mErrorView);
            mErrorView = null;
        }
        mErrorStatus = status;
    }

    public void setNetErrorStatus(MultipleStatus status){
        if(mNoNetworkView != null){
            removeView(mNoNetworkView);
            mNoNetworkView = null;
        }
        mNoNetworkStatus = status;
    }

    /**
     * 显示加载中视图
     * @param hint 自定义提示文本内容
     */
    public final void showLoading(String hint) {
        changeViewStatus(STATUS_LOADING);
        if (null == mLoadingView) {
            mLoadingView = mLoadingStatus.getView(getContext(), this);
            addView(mLoadingView, 0);
        }
        showViewByStatus(mLoadingView);
        mLoadingStatus.showMessage(mLoadingView, hint);
    }

    /**
     * 显示错误视图
     * @param hint 自定义提示文本内容
     */
    public final void showError(String hint) {
        changeViewStatus(STATUS_ERROR);
        if (null == mErrorView) {
            mErrorView = mErrorStatus.getView(getContext(), this);
            View clickView = mErrorStatus.getRetryView(mErrorView);
            if (clickView != null) {
                clickView.setOnClickListener(mOnRetryClickListener);
            }
            addView(mErrorView, 0);
        }
        showViewByStatus(mErrorView);
        mErrorStatus.showMessage(mErrorView, hint);
    }


    /**
     * 显示空视图
     * @param hint 自定义提示文本内容
     */
    public final void showEmpty(String hint) {
        changeViewStatus(STATUS_EMPTY);
        if (null == mEmptyView) {
            mEmptyView = mEmptyStatus.getView(getContext(), this);
            View clickView = mEmptyStatus.getRetryView(mEmptyView);
            if (clickView != null) {
                clickView.setOnClickListener(mOnRetryClickListener);
            }
            addView(mEmptyView, 0);
        }
        showViewByStatus(mEmptyView);
        mEmptyStatus.showMessage(mEmptyView, hint);
    }

    /**
     * 显示无网络视图
     *
     * @param hint 自定义提示文本内容
     */
    public final void showNoNetwork(String hint) {
        changeViewStatus(STATUS_NO_NETWORK);
        if (null == mNoNetworkView) {
            mNoNetworkView = mNoNetworkStatus.getView(getContext(), this);
            View clickView = mNoNetworkStatus.getRetryView(mNoNetworkView);
            if (clickView != null) {
                clickView.setOnClickListener(mOnRetryClickListener);
            }
            addView(mNoNetworkView, 0);
        }
        showViewByStatus(mNoNetworkView);
        mNoNetworkStatus.showMessage(mNoNetworkView, hint);
    }


    /**
     * 显示内容视图
     */
    public final void showContent() {
        changeViewStatus(STATUS_CONTENT);
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if(view == mLoadingView){
                view.setVisibility(View.GONE);
            }else if(view == mEmptyView){
                view.setVisibility(View.GONE);
            }else if(view == mErrorView){
                view.setVisibility(View.GONE);
            }else if(view == mNoNetworkView){
                view.setVisibility(View.GONE);
            }else{
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showViewByStatus(View statusView) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            view.setVisibility(view == statusView ? View.VISIBLE : View.GONE);
        }
    }

    private void checkNull(Object object, String hint) {
        if (null == object) {
            throw new NullPointerException(hint);
        }
    }

    private void clear(View... views) {
        if (null == views) {
            return;
        }
        try {
            for (View view : views) {
                if (null != view) {
                    removeView(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 视图状态改变接口
     */
    public interface OnViewStatusChangeListener {

        /**
         * 视图状态改变时回调
         *
         * @param oldViewStatus 之前的视图状态
         * @param newViewStatus 新的视图状态
         */
        void onChange(int oldViewStatus, int newViewStatus);
    }

    /**
     * 设置视图状态改变监听事件
     *
     * @param onViewStatusChangeListener 视图状态改变监听事件
     */
    public void setOnViewStatusChangeListener(OnViewStatusChangeListener onViewStatusChangeListener) {
        this.mViewStatusListener = onViewStatusChangeListener;
    }

    /**
     * 改变视图状态
     *
     * @param newViewStatus 新的视图状态
     */
    private void changeViewStatus(int newViewStatus) {
        if (mViewStatus == newViewStatus) {
            return;
        }
        if (null != mViewStatusListener) {
            mViewStatusListener.onChange(mViewStatus, newViewStatus);
        }
        mViewStatus = newViewStatus;
    }

}
