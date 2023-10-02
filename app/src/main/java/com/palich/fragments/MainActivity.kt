package com.palich.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.app.Application
import android.content.SharedPreferences

class MyApplication : Application() {
    companion object {
        private const val TAG = "MyApplication"

        var instance: MyApplication? = null

        val api = SuperHeroApiClient().client.create(SuperHeroApiInterface::class.java)
        private var superGeroList: MutableList<SuperHero>? = null

        suspend fun getSuperHeroList(): MutableList<SuperHero>? {
            Log.i(TAG, "getSuperHeroList")
            if (superGeroList != null) {
                return superGeroList
            }
            return fetchSuperHeroList()
        }

        private suspend fun fetchSuperHeroList(): MutableList<SuperHero>? {
            Log.i(TAG, "fetchSuperHeroList")
            if (superGeroList == null) {
                Log.i(TAG, "Super heroes list before")
                superGeroList = api.getSuperHero()
                Log.i(TAG, "Super heroes list after length ${superGeroList?.count()}")
            }
            Log.i(TAG, "Super heroes list $superGeroList")
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
}