package com.mellivora.demo.adapter

import androidx.recyclerview.widget.RecyclerView


abstract class BaseRecyclerAdapter<T>(beans: MutableList<out T>? = null) : RecyclerView.Adapter<RecyclerHolder>() {

    private var adapterBeans: MutableList<T>? = null
    open val datas: MutableList<T>? get() = adapterBeans   //用于给外部调用的getDatas方法

    init {
        if (beans != null) {
            adapterBeans = mutableListOf()
            adapterBeans!!.addAll(beans)
        }
    }

    open fun getItem(position: Int): T? {
        return if (position < adapterBeans?.size ?: 0) adapterBeans?.get(position) else null
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        if (getItem(position) == null) {

        } else {
            onBindViewHolder(holder, getItem(position)!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        //use onBindViewHolder(holder: RecyclerHolder, position: Int)
        return super.getItemViewType(position)
    }


    //    /** @deprecated link onBindViewHolder(holder: RecyclerHolder, position: Int)
//     * if you need to customize itemType (when you add a Header with PullView)
//     * holder.adapterPosition will be incorrectly positioned
//     */
//    @Deprecated("")
    open fun onBindViewHolder(holder: RecyclerHolder, bean: T) {
    }


    /** 刷新布局
     * @see notifyDataSetChanged()
     */
    open fun notifyDataChanged() {
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return adapterBeans?.size ?: 0
    }


    open fun remove(position: Int) {
        if (adapterBeans != null && adapterBeans!!.size > position && position >= 0) {
            adapterBeans!!.removeAt(position)
            notifyDataChanged()
        }
    }

    fun removeAnim(position: Int) {
        if (adapterBeans?.isNotEmpty() == true && adapterBeans!!.size > position && position >= 0) {
            adapterBeans?.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }


    open fun addNotify(addBean: T?, index: Int = datas?.size ?: 0) {
        if (addBean == null) return
        if (adapterBeans == null) adapterBeans = mutableListOf()
        adapterBeans!!.add(index, addBean)
        notifyDataChanged()
    }

    open fun addNotify(addBeans: MutableList<out T>?) {
        if (addBeans != null) {
            if (adapterBeans == null) adapterBeans = mutableListOf()
            adapterBeans!!.addAll(addBeans)
        }
        notifyDataChanged()
    }

    open fun resetNotify(resetBeans: MutableList<out T>?) {
        if (resetBeans == null) {
            adapterBeans = null
        } else {
            if (adapterBeans == null) adapterBeans = mutableListOf()
            adapterBeans!!.clear()
            adapterBeans!!.addAll(resetBeans)
        }
        notifyDataChanged()
    }


    open fun cleanNotify() {
        adapterBeans?.clear()
        notifyDataChanged()
    }
}
