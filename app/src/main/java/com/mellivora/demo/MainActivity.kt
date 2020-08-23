package com.mellivora.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mellivora.demo.adapter.BaseRecyclerAdapter
import com.mellivora.demo.adapter.RecyclerHolder
import com.mellivora.demo.refresh.DemoErrorStatus
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_data.view.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pullView.swipeRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DataAdapter()
        pullView.setAdapter(adapter)

        pullView.statusView.setErrorStatus(DemoErrorStatus())

        pullView.setPullEnable(true, true)
        pullView.setPullListener { isRefresh, page -> loadData(isRefresh, page) }
        pullView.showLoading()

        btnRefresh.setOnClickListener {
            if(pullView.isRefreshing || pullView.isLoading) return@setOnClickListener
            pullView.showLoading()
        }
    }


    private fun loadData(isRefresh: Boolean, page: Int){
        GlobalScope.launch(Dispatchers.Main) {
            val datas = withContext(Dispatchers.Default) {
                delay(1500)
                val data = mutableListOf<String>()
                for(i in 1 until 11){
                    data.add("数据${page*10+i}")
                }
                data
            }
            if(isRefresh){
                adapter.resetNotify(datas)
            }else{
                adapter.addNotify(datas)
            }
            pullView.loadFinish(isRefresh, page < 5, false)
        }
    }

   private inner class DataAdapter : BaseRecyclerAdapter<String>() {
       override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
           return RecyclerHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false))
       }

       override fun onBindViewHolder(holder: RecyclerHolder, bean: String) {
           holder.itemView.tvData.text = bean
       }
   }



}