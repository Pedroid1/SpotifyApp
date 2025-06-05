package com.pedroid.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.pedroid.common.BaseFragment
import com.pedroid.common.ext.setUserProfile
import com.pedroid.common.ext.showSnackBar
import com.pedroid.common.livedata.EventObserver
import com.pedroid.feature.home.R
import com.pedroid.feature.home.databinding.FragmentHomeBinding
import com.pedroid.model.UserProfile
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
        _binding.vm = viewModel
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
                    viewModel.uiState.collectLatest {
                        handleUserProfile(it.userProfile)
                    }
                }
            }
        }
        viewModel.errorEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                requireView().showSnackBar(it.asString(requireContext()), Snackbar.LENGTH_SHORT)
            }
        )
    }

    private fun handleUserProfile(userProfile: UserProfile?) {
        userProfile?.let {
            setUserProfile(
                _binding.profileImageview,
                _binding.initialsNameTxt,
                userProfile.displayName,
                userProfile.imageUrl,
                com.pedroid.core.design_system.R.color.profile_background_color
            )
        }
    }
}
