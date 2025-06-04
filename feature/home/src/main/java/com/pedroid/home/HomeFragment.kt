package com.pedroid.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.pedroid.common.BaseFragment
import com.pedroid.feature.home.R
import com.pedroid.feature.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val onArtistClick: (id: String) -> Unit = { id ->
        //TODO Go to ArtistAlbum
    }

    private val viewModel: HomeViewModel by viewModels()
    private val adapter by lazy { ArtistAdapter(onArtistClick) }

    override fun initialWork() {
        _binding.recycler.adapter = adapter
    }

    override fun setupViewModel() {

    }

    override fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.artists.collect { data ->
                    adapter.submitData(data)
                }
            }
        }
    }
}