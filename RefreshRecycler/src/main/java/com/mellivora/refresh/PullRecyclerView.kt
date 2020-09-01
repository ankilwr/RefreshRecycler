package com.mellivora.refresh

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.mellivora.multiple.MultipleStatusView
import com.mellivora.error.PullErrorStatus
import com.mellivora.pullview.R
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.mellivora.swipe.SwipeMenuRecyclerView


class PullRecyclerView(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs), RefreshResultInterface {

    constructor(context: Context) : this(context, null)

    override var page = 1

    val refreshLayout: SmartRefreshLayout
    val swipeRecyclerView: SwipeMenuRecyclerView
    val statusView: MultipleStatusView

    private var footerLineView: View? = null
    private var headerEnable: Boolean = false
    private var footerEnable: Boolean = false
    private var hasMore: Boolean = false

    private var emptyHint: String = "暂无内容"

    private var pullAdapterObserver: RecyclerView.AdapterDataObserver? = null
    private var loadListener: OnRefreshLoadMoreListener? = null
    private var onViewStatusChangeListener: MultipleStatusView.OnViewStatusChangeListener? = null
    private var onScrollListener: ScrollListener? = null


    val isRefreshing: Boolean
        get() = refreshLayout.state == RefreshState.Refreshing

    val isLoading: Boolean
        get() = statusView.isLoading

    val isSuccess: Boolean
        get() = statusView.isSuccess


    init {
        View.inflate(getContext(), R.layout.layout_pull_to_refresh, this)
        refreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRecyclerView = findViewById(R.id.recyclerView)
        statusView = findViewById(R.id.statusView)
        statusView.setOnRetryClickListener {
            if (loadListener == null) return@setOnRetryClickListener
            showLoading()
            //loadListener!!.onRefresh(refreshLayout)
        }
        statusView.setOnViewStatusChangeListener { oldViewStatus, newViewStatus ->
            onViewStatusChangeListener?.onChange(oldViewStatus, newViewStatus)
            when (newViewStatus) {
                MultipleStatusView.STATUS_CONTENT -> {
                    refreshLayout.setEnableRefresh(headerEnable)
                    refreshLayout.setEnableLoadMore(footerEnable)
                }
                MultipleStatusView.STATUS_EMPTY -> {
                    refreshLayout.setEnableRefresh(headerEnable)
                    refreshLayout.setEnableLoadMore(false)
                }
                else -> {
                    refreshLayout.setEnableRefresh(false)
                }
            }
        }
        setPullEnable(header = false, footer = false)
    }

    override fun onDetachedFromWindow() {
        if (swipeRecyclerView.originAdapter != null && pullAdapterObserver != null) {
            swipeRecyclerView.originAdapter.unregisterAdapterDataObserver(pullAdapterObserver!!)
            pullAdapterObserver = null
        }
        footerLineView?.parent?.let {
            (it as ViewGroup).removeView(footerLineView)
        }
        footerLineView = null
        super.onDetachedFromWindow()
    }


    private fun onSizeChange(count: Int, emptyView: View?, isAdapterEmpty: Boolean) {
        if (isAdapterEmpty || emptyView != null) {
            //当isAdapterEmpty为true时, emptyView为空也要出现内容，因为有Adapter添加头部的场景
            refreshLayout.setEnableRefresh(headerEnable)
            emptyView?.visibility = if (count > 0) View.GONE else View.VISIBLE
            return
        }
        if (count > 0) {
            statusView.showContent()
        } else {
            statusView.showEmpty(emptyHint)
        }
    }


    fun setEmptyHint(emptyHint: String) {
        this.emptyHint = emptyHint
    }

    fun setPullEnable(header: Boolean, footer: Boolean) {
        this.headerEnable = header
        this.footerEnable = footer
        refreshLayout.setEnableRefresh(header)
        refreshLayout.setEnableLoadMore(footer)
    }


    fun pullRefreshing(notify: Boolean) {
        if (isRefreshing) {
            if (notify) loadListener?.onRefresh(refreshLayout)
            return
        }
        if (notify) {
            refreshLayout.autoRefresh()
        } else {
            refreshLayout.autoRefreshAnimationOnly()
        }
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>?, adapterEmptyView: View? = null, isAdapterView: Boolean = false) {
        onSizeChange(adapter?.itemCount ?: 0, adapterEmptyView, isAdapterView)

        if (swipeRecyclerView.originAdapter != null && pullAdapterObserver != null) {
            swipeRecyclerView.originAdapter.unregisterAdapterDataObserver(pullAdapterObserver!!)
        }
        swipeRecyclerView.adapter = adapter
        if (adapter != null) {
            pullAdapterObserver = object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    onSizeChange(adapter.itemCount, adapterEmptyView, isAdapterView)
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    onSizeChange(adapter.itemCount, adapterEmptyView, isAdapterView)
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                    onSizeChange(adapter.itemCount, adapterEmptyView, isAdapterView)
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    onSizeChange(adapter.itemCount, adapterEmptyView, isAdapterView)
                }

                override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                    onSizeChange(adapter.itemCount, adapterEmptyView, isAdapterView)
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    onSizeChange(adapter.itemCount, adapterEmptyView, isAdapterView)
                }
            }
            adapter.registerAdapterDataObserver(pullAdapterObserver!!)
        }
    }

    fun setPullListener(onLoadData: (isRefresh: Boolean, page: Int) -> Unit) {
        loadListener = object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                onLoadData(true, 1)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                onLoadData(false, page)
            }
        }
        refreshLayout.setOnRefreshLoadMoreListener(loadListener)
    }

    fun setOnViewStatusChangeListener(listener: MultipleStatusView.OnViewStatusChangeListener) {
        this.onViewStatusChangeListener = listener
    }

    fun setOnScrollListener(listener: RecyclerView.OnScrollListener?) {
        if (listener != null) {
            onScrollListener?.let { swipeRecyclerView.removeOnScrollListener(it) }
            onScrollListener = ScrollListener(listener)
            swipeRecyclerView.addOnScrollListener(onScrollListener!!)
        } else {
            onScrollListener?.let { swipeRecyclerView.removeOnScrollListener(it) }
            onScrollListener = null
        }
    }


    /**
     * 调用前记得取消相关请求任务
     */
    fun showLoading(notify: Boolean = true, message: String? = "正在加载") {
        statusView.showLoading(message)
        refreshLayout.finishRefresh(0)
        refreshLayout.finishLoadMore(0)
        refreshLayout.setEnableRefresh(false)
        refreshLayout.setEnableLoadMore(false)
        if (notify) loadListener?.onRefresh(refreshLayout)
    }


    /** 一般作为非下拉刷新列表时使用 */
    fun showContent() {
        loadFinish(isRefresh = true, hasMore = false)
    }

    override fun loadError(isRefresh: Boolean, message: String?, code: Int) {
        if (statusView.isLoading) {
            if (code == PullErrorStatus.NETWORK_ERROR) {
                statusView.showNoNetwork(message)
            } else {
                statusView.showError(message)
            }
            return
        }
        if (isRefresh) {
            refreshLayout.finishRefresh(false)
        } else {
            refreshLayout.finishLoadMore(false)
        }
    }


    /**
     * @param isRefresh:是否是 true:刷新 (false: 加载更多)
     * @param hasMore : true:可以加载更多, false:已经是最有一页
     * @param footerDivider : true:显示提示item(如：----已经到底了----), false:不显示提示item
     */
    override fun loadFinish(isRefresh: Boolean, hasMore: Boolean, footerDivider: Boolean) {
        if (statusView.isLoading) statusView.showContent()
        this.hasMore = hasMore
        if (isRefresh) {
            page = if (hasMore) 2 else 1
            refreshLayout.finishRefresh(true)
            refreshLayout.setNoMoreData(!hasMore)
        } else {
            if (hasMore) page += 1
            swipeRecyclerView.stopScroll()
            refreshLayout.finishLoadMore(0, true, !hasMore)
        }
        if (!footerDivider) return
        footerLineView?.let {
            swipeRecyclerView.removeFooterView(it)
        }
        if (!hasMore) {
            if (page <= 1) return
            if (footerLineView == null) footerLineView =
                LayoutInflater.from(context).inflate(R.layout.footer_line, swipeRecyclerView, false)
            swipeRecyclerView.addFooterView(footerLineView)
        }
    }


    private class ScrollListener(val scrollListener: RecyclerView.OnScrollListener) :
        RecyclerView.OnScrollListener() {
        var scrollX = 0
            private set
        var scrollY = 0
            private set

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            scrollX += dx
            scrollY += dy
            scrollListener.onScrolled(recyclerView, scrollX, scrollY)
        }
    }


}