package com.palich.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsFragment() : Fragment() {
    companion object {
        private const val TAG = "DetailsFragment"
    }

    private var text: TextView? = null
    private var image: ImageView? = null
    private var text2: TextView? = null

    private var superHero: SuperHero? = null
    private val myScope = CoroutineScope(Dispatchers.Main)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text = view.findViewById(R.id.details_textView)
        text2 = view.findViewById(R.id.details_textView2)
        image = view.findViewById(R.id.details_imageView)
    }

    fun show(ii: Int) {
        myScope.launch {
            val sgList = MyApplication.getSuperHeroList()
            Log.i(TAG, "Super heroes list length ${sgList?.count()}")
            superHero = if (sgList != null) {
                sgList[ii]
            } else {
                null
            }
            text?.text = superHero?.name
            text2?.text = superHero?.biography?.fullName
            val url = superHero?.images?.md ?: ""
            if (image != null) {
                Glide.with(requireContext()).load(url).into(image!!)
            }
        }
    }

}
