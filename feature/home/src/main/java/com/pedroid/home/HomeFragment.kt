package com.pedroid.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.pedroid.common.BaseFragment
import com.pedroid.feature.home.R
import com.pedroid.feature.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private val adapter = ArtistAdapter()

    override fun initialWork() {
        _binding.recycler.adapter = adapter
        lifecycleScope.launch {
            viewModel.artists.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun setupViewModel() {

    }

    override fun setupObservers() {

    }

}