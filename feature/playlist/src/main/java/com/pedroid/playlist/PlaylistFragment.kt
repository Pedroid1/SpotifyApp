package com.pedroid.playlist

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.pedroid.common.BaseFragment
import com.pedroid.common.ClickUtil
import com.pedroid.common.ext.observe
import com.pedroid.common.ext.setUserProfile
import com.pedroid.common.ext.showSnackBar
import com.pedroid.common.livedata.EventObserver
import com.pedroid.feature.playlist.R
import com.pedroid.feature.playlist.databinding.FragmentPlaylistBinding
import com.pedroid.model.UserProfile
import com.pedroid.playlist.dialog.CreatePlaylistDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlaylistFragment : BaseFragment<FragmentPlaylistBinding>(R.layout.fragment_playlist) {

    private val onCreatePlaylist: (String) -> Unit = { name ->
        viewModel.createPlaylist(name)
    }

    private val viewModel: PlaylistViewModel by viewModels()
    private val adapter by lazy { PlaylistAdapter() }
    private var dialogFragment: CreatePlaylistDialog? = null

    override fun initialWork() {
        _binding.recycler.adapter = adapter
        _binding.vm = viewModel
        setupListeners()
    }

    private fun setupListeners() {
        _binding.createPlaylistBtn.setOnClickListener {
            if (ClickUtil.isFastDoubleClick) return@setOnClickListener
            showCreatePlaylistDialog()
        }
    }

    private fun showCreatePlaylistDialog() {
        dialogFragment = CreatePlaylistDialog(onCreatePlaylist)
        dialogFragment?.show(requireActivity().supportFragmentManager, null)
    }

    override fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.playlists.collectLatest { data ->
                        adapter.submitData(data)
                    }
                }
                launch {
                    adapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                        .filter { it.refresh is LoadState.NotLoading }
                        .collect {
                            _binding.recycler.smoothScrollToPosition(0)
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
        viewModel.refreshTrigger.observe(
            viewLifecycleOwner,
            EventObserver {
                adapter.refresh()
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
