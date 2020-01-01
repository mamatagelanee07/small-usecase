package com.andigeeky.mvpapp.ui.lines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.andigeeky.mvpapp.R
import com.andigeeky.mvpapp.databinding.FragmentHomeBinding
import com.andigeeky.mvpapp.di.Injectable
import com.andigeeky.mvpapp.ui.common.AppExecutors
import com.andigeeky.mvpapp.ui.common.FragmentDataBindingComponent
import com.andigeeky.mvpapp.util.autoCleared
import timber.log.Timber
import javax.inject.Inject

/**
 * The UI Controller for displaying all Lines information of TFL.
 */
class LinesFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: AppExecutors

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    private var adapter by autoCleared<LinesAdapter>()
    private var binding by autoCleared<FragmentHomeBinding>()

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
        binding.lifecycleOwner = viewLifecycleOwner
        binding.lines = lineStatusViewModel.getLines()
        this.adapter = LinesAdapter(dataBindingComponent,appExecutors){
            Timber.e("Clicked $it")
        }
        binding.listLines.adapter = adapter

        lineStatusViewModel.getLines().observe(viewLifecycleOwner, Observer {
            val lines = it.data ?: emptyList()
            adapter.submitList(lines)
        })
    }

    fun navController() = findNavController()
}
