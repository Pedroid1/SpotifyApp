package com.pedroid.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.pedroid.analytics.Constants
import com.pedroid.common.base.BaseFragment
import com.pedroid.common.extension.setUserProfile
import com.pedroid.common.extension.showSnackBar
import com.pedroid.common.livedata.EventObserver
import com.pedroid.feature.home.R
import com.pedroid.feature.home.databinding.FragmentHomeBinding
import com.pedroid.model.Artist
import com.pedroid.model.UserProfile
import com.pedroid.navigation.features.HomeNavigation
import com.pedroid.publicmodule.AlbumsFeatureCommunicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    @Inject
    lateinit var albumsFeatureCommunicator: AlbumsFeatureCommunicator

    private val onArtistClick: (artist: Artist) -> Unit = { artist ->
        logOnClickArtistEvent(artist)
        albumsFeatureCommunicator.launchFeature(
            AlbumsFeatureCommunicator.AlbumsFeatureArgs(
                HomeNavigation.ROUTE,
                artist
            )
        )
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

    private fun logOnClickArtistEvent(artist: Artist) {
        analytics.logEvent(
            Constants.SELECT_ITEM,
            mapOf(
                Constants.SELECT_ITEM_ID to artist.id,
                Constants.SELECT_ITEM_NAME to artist.name
            )
        )
    }
}
