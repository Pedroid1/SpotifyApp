package com.pedroid.home

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.pedroid.common.BaseFragment
import com.pedroid.common.GeneralUtils
import com.pedroid.common.ext.loadImage
import com.pedroid.feature.home.R
import com.pedroid.feature.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val onArtistClick: (id: String) -> Unit = { id ->
        // TODO Go to ArtistAlbum
    }

    private val viewModel: HomeViewModel by viewModels()
    private val adapter by lazy { ArtistAdapter(onArtistClick) }

    override fun initialWork() {
        _binding.recycler.adapter = adapter
    }

    override fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.artists.collect { data ->
                        adapter.submitData(data)
                    }
                }

                launch {
                    viewModel.state.collectLatest {
                        it.userPhotoUrl?.let { url ->
                            _binding.profileImageview.backgroundTintList = null
                            _binding.profileImageview.background = null
                            _binding.initialsNameTxt.visibility = View.GONE
                            _binding.profileImageview.loadImage(
                                url = url
                            )
                        } ?: it.userName?.let { username ->
                            _binding.profileImageview.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    com.pedroid.core.design_system.R.color.profile_background_color
                                )
                            )
                            _binding.initialsNameTxt.visibility = View.VISIBLE
                            _binding.initialsNameTxt.text =
                                GeneralUtils.getInitials(username)
                        }
                    }
                }
            }
        }
    }
}
