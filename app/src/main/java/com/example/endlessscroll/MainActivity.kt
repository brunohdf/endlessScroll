package com.example.endlessscroll

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.example.endlessscroll.endless.EndlessScrollListener
import android.support.v7.widget.LinearLayoutManager
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayoutManager = LinearLayoutManager(this)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = linearLayoutManager
        var list = mutableListOf<String>()
        val adapter = Adapter(list)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object: EndlessScrollListener(linearLayoutManager) {
            override fun onLoadMore(current_page: Int) {
                fetchData(list, adapter, current_page)
            }
        })

        fetchData(list, adapter)
    }

    fun fetchData(list: MutableList<String>, adapter: Adapter, page: Int = 1) {
        list.addAll(getPage(adapter.itemCount, page))
        adapter.notifyDataSetChanged()
        Log.d(this.javaClass.name, "fetchind page: $page")
    }

    private fun getPage(size: Int, page: Int): List<String> {
        var list = mutableListOf<String>()
        for (i in 1..10) {
            list.add("""${page} -> ${(i + size)}""")
        }

        return list
    }
}
