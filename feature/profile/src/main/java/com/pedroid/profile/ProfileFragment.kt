package com.pedroid.profile

import androidx.fragment.app.viewModels
import com.pedroid.common.BaseFragment
import com.pedroid.common.ext.observe
import com.pedroid.common.ext.setUserProfile
import com.pedroid.feature.profile.R
import com.pedroid.feature.profile.databinding.FragmentProfileBinding
import com.pedroid.model.UserProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()

    override fun initialWork() {
        _binding.vm = viewModel
    }

    override fun setupObservers() {
        viewLifecycleOwner.observe(viewModel.userProfile, ::handleUserProfile)
    }

    private fun handleUserProfile(userProfile: UserProfile) {
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