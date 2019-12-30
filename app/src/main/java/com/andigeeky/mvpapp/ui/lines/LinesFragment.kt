package com.andigeeky.mvpapp.ui.lines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.andigeeky.mvpapp.R
import com.andigeeky.mvpapp.databinding.FragmentHomeBinding
import com.andigeeky.mvpapp.di.Injectable
import com.andigeeky.mvpapp.util.autoCleared
import timber.log.Timber
import javax.inject.Inject

/**
 * The UI Controller for displaying a Github Repo's information with its contributors.
 */
class LinesFragment : Fragment(), Injectable {

    private var binding by autoCleared<FragmentHomeBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var lineStatusViewModel: LinesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_home,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lineStatusViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(LinesViewModel::class.java)

        lineStatusViewModel.getLines().observe(viewLifecycleOwner, Observer {
            Timber.e(it.toString())
        })
    }

    fun navController() = findNavController()
}
