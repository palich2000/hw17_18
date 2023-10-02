package com.palich.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
class ListFragment : Fragment() {
    companion object {
        private const val TAG = "ListFragment"
    }
    private val myScope = CoroutineScope(Dispatchers.Main)
    private var onItemClick: (Int, Boolean) -> Unit = { _, _ ->}
    private var post: () -> Unit = {}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }


    private suspend fun getSuperHeroList(): MutableList<SuperHero>? {
        val sgList:MutableList<SuperHero>? = MyApplication.getSuperHeroList()
        if (sgList != null) {
            val listView: RecyclerView? = view?.findViewById(R.id.list_recycler_view)
            if (listView != null) {
                val adapter = listView.adapter as MyRecyclerViewAdapter
                adapter.updateList(sgList)
                listView.scrollToPosition(MyApplication.loadCurrentPosition())
                onItemClick(MyApplication.loadCurrentPosition(), true)
            }
        }
        return sgList
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

        myScope.launch {
            getSuperHeroList()
        }

    }

    fun setPost(post: () -> Unit) {
        this.post = post
    }

    fun setOnItemClickListener(listener: (Int, Boolean) -> Unit) {
        onItemClick = listener
    }

    override fun onDestroy() {
        super.onDestroy()
        myScope.cancel()
    }
}
