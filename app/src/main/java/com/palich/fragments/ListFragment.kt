package com.palich.fragments

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListFragment : Fragment() {
    companion object {
        private const val TAG = "ListFragment"
    }

    private var onItemClick: (Int, Boolean) -> Unit = { _, _ ->}
    private var post: () -> Unit = {}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listView: RecyclerView = view.findViewById(R.id.list_recycler_view)

        val adapter = MyRecyclerViewAdapter(mutableListOf()) {
            Log.i(TAG, "Clicked on $it")
            onItemClick(it, false)
        }

        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(requireContext())
        MyApplication.getSuperHeroList {
            if (it != null) {
                adapter.updateList(it)
                listView.scrollToPosition(MyApplication.loadCurrentPosition())
                onItemClick(MyApplication.loadCurrentPosition(), true)
            }
        }

        listView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visiblePosition = layoutManager.findFirstVisibleItemPosition()
                if (visiblePosition != 0) {
                    MyApplication.saveCurrentPosition(visiblePosition)
                }
            }
        })

//        listView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
//            override fun onPreDraw(): Boolean {
//                listView.viewTreeObserver.removeOnPreDrawListener(this)
//
//                return true
//            }
//        })
    }

    fun setPost(post: () -> Unit) {
        this.post = post
    }

    fun setOnItemClickListener(listener: (Int, Boolean) -> Unit) {
        onItemClick = listener
    }
}
