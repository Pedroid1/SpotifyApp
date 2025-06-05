package com.pedroid.privatemodule

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.pedroid.common.BaseFragment
import com.pedroid.common.ext.loadImage
import com.pedroid.feature.albums.privatemodule.R
import com.pedroid.feature.albums.privatemodule.databinding.FragmentAlbumsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumsFragment : BaseFragment<FragmentAlbumsBinding>(R.layout.fragment_albums) {

    private val viewModel: AlbumsViewModel by viewModels()
    private val adapter by lazy { AlbumsAdapter() }

    override fun initialWork() {
        _binding.recycler.adapter = adapter
        _binding.vm = viewModel
        setupListeners()
    }

    private fun setupListeners() {
        _binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collectLatest { state ->
                        handleUiState(state)
                    }
                }
                launch {
                    viewModel.albums.collectLatest {
                        adapter.submitData(it)
                    }
                }
            }
        }
    }

    private fun handleUiState(state: AlbumsUiState) {
        state.artist?.let { artist ->
            with(_binding) {
                artistName.text = artist.name
                artistImage.loadImage(
                    url = artist.imageUrl,
                    isCircular = true
                )
            }
        }
    }
}