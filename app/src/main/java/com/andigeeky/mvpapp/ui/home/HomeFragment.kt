package com.andigeeky.mvpapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andigeeky.mvpapp.R
import com.andigeeky.mvpapp.di.Injectable

/**
 * The UI Controller for displaying a Github Repo's information with its contributors.
 */
class HomeFragment : Fragment(), Injectable {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_main, null)
    }
}
