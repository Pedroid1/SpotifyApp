package com.pedroid.playlist

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.pedroid.common.BaseFragment
import com.pedroid.common.ext.observe
import com.pedroid.common.ext.setUserProfile
import com.pedroid.common.ext.showSnackBar
import com.pedroid.common.livedata.EventObserver
import com.pedroid.feature.playlist.R
import com.pedroid.feature.playlist.databinding.FragmentPlaylistBinding
import com.pedroid.model.UserProfile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlaylistFragment : BaseFragment<FragmentPlaylistBinding>(R.layout.fragment_playlist) {

    private val viewModel: PlaylistViewModel by viewModels()
    private val adapter by lazy { PlaylistAdapter() }

    override fun initialWork() {
        _binding.recycler.adapter = adapter
        _binding.vm = viewModel
    }

    override fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.playlists.collect { data ->
                        adapter.submitData(data)
                    }
                }
            }
        }
        viewLifecycleOwner.observe(viewModel.userProfile, ::handleUserInfo)
        viewModel.errorEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                requireView().showSnackBar(it.asString(requireContext()), Snackbar.LENGTH_SHORT)
            }
        )
    }

    private fun handleUserInfo(userProfile: UserProfile) {
        setUserProfile(
            _binding.profileImageview,
            _binding.initialsNameTxt,
            userProfile.displayName,
            userProfile.imageUrl,
            com.pedroid.core.design_system.R.color.profile_background_color
        )
    }
}