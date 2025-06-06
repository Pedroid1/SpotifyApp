package com.pedroid.profile

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
import com.pedroid.common.utils.ClickUtil
import com.pedroid.feature.profile.R
import com.pedroid.feature.profile.databinding.FragmentProfileBinding
import com.pedroid.model.UserProfile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()

    override fun initialWork() {
        _binding.vm = viewModel
        setupListeners()
    }

    private fun setupListeners() {
        _binding.exitBtn.setOnClickListener {
            if (ClickUtil.isFastDoubleClick) return@setOnClickListener
            logLogoutEvent()
            viewModel.logout()
        }
    }

    override fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest {
                    handleUserProfile(it.userProfile)
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
            _binding.name.text = userProfile.displayName
            setUserProfile(
                _binding.image,
                _binding.initials,
                userProfile.displayName,
                userProfile.imageUrl,
                com.pedroid.core.design_system.R.color.profile_background_color
            )
        }
    }

    private fun logLogoutEvent() {
        analytics.logEvent(
            Constants.LOGOUT_EVENT,
            mapOf(
                Constants.LOGOUT_METHOD to Constants.LOGOUT_MANUAL
            )
        )
    }
}
