package com.palich.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.app.Application
import android.content.SharedPreferences
import android.os.PersistableBundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyApplication : Application() {
    companion object {
        private const val TAG = "MyApplication"
        var instance: MyApplication? = null
        val api = SuperHeroApiClient().client.create(SuperHeroApiInterface::class.java)
        private var superGeroList: MutableList<SuperHero>? = null

        fun setSuperHeroList(list: MutableList<SuperHero>?) {
            superGeroList = list
        }

        var cbSuperHeroes: (MutableList<SuperHero>?) -> Unit = {}

        fun getSuperHeroList(cb: (MutableList<SuperHero>?) -> Unit): MutableList<SuperHero>? {
            cbSuperHeroes = cb
            if (superGeroList != null) {
                cb(superGeroList)
                return superGeroList
            }
            return null
        }

        fun fetchSuperHeroList(): MutableList<SuperHero>? {
            if (superGeroList != null) {
                return superGeroList
            }
            val s = api.getSuperHero()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isNotEmpty()) {
                        setSuperHeroList(it)
                        cbSuperHeroes(it)
                        Log.i(TAG, "Super heroes list length ${it.count()}")
                    } else {
                        Log.e(TAG, "No super heroes")
                    }
                }, {
                    Log.e(TAG, "Error $it")
                })
            return superGeroList
        }

        private lateinit var sharedPrefs: SharedPreferences
        fun saveCurrentPosition(position: Int) {
            Log.i(TAG, "saveCurrentPosition $position")
            save("position", position.toString())
        }

        fun loadCurrentPosition(): Int {
            val position = load("position")
            Log.i(TAG, "loadCurrentPosition $position")
            return position?.toInt() ?: 0
        }

        private fun save(key: String, value: String) {
            sharedPrefs.edit().putString(key, value).apply()
        }

        private fun load(key: String): String? {
            return sharedPrefs.getString(key, null)
        }
    }
    override fun onCreate() {
        super.onCreate()
        Log.i("Application", "onCreate")
        instance = this
        fetchSuperHeroList()
        sharedPrefs = getSharedPreferences("my_prefs", MODE_PRIVATE)
    }


}

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        Log.i("MainActivity", "onCreate ${supportFragmentManager.fragments}")
        supportFragmentManager.beginTransaction().commit().let {
            supportFragmentManager.executePendingTransactions()
        }
        val listFragment =
            supportFragmentManager.findFragmentById(R.id.list_fragment) as ListFragment
        val detailsFragment =
            supportFragmentManager.findFragmentById(R.id.details_fragment) as? DetailsFragment

        var fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            Log.i("MainActivity", "Current position: ${MyApplication.loadCurrentPosition()}")
        }

        listFragment.setOnItemClickListener { index, force ->
            MyApplication.saveCurrentPosition(index)
            if (detailsFragment != null) {
                detailsFragment.show(index)
            } else {
                if (!force) {
                    val detailsFragment = DetailsFragment()

                    supportFragmentManager.beginTransaction()
                        .add(R.id.list_fragment, detailsFragment)
                        .addToBackStack("details")
                        .commit().let {
                            supportFragmentManager.executePendingTransactions()
                            Log.i("MainActivity", "supportFragmentManager add $it")
                            detailsFragment.show(index)
                        }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainActivity", "onDestroy")
        val detailsFragment =
            supportFragmentManager.findFragmentById(R.id.details_fragment) as? DetailsFragment
        if (detailsFragment != null) {
            supportFragmentManager.beginTransaction()
                .remove(detailsFragment)
                .commit()
        }
        Log.i("MainActivity", "onDestroy ${supportFragmentManager.fragments}")
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState, outPersistentState)
//    }
}