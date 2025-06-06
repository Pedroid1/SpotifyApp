package com.pedroid.playlist.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.pedroid.analytics.Constants
import com.pedroid.analytics.IAnalyticsEventLogger
import com.pedroid.feature.playlist.R
import com.pedroid.feature.playlist.databinding.DialogCreatePlaylistBinding

class CreatePlaylistDialog(
    private val onCreatePlaylist: (String) -> Unit,
    private val analytics: IAnalyticsEventLogger
) : DialogFragment() {

    private lateinit var binding: DialogCreatePlaylistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.pedroid.core.design_system.R.style.OverlayScreenDialogTheme)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (resources.displayMetrics.heightPixels * 0.95).toInt()
            )
            setGravity(Gravity.BOTTOM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreatePlaylistBinding.inflate(inflater, container, false)

        binding.createPlaylistBtn.setOnClickListener {
            val playlistName = binding.edtPlaylistName.text.toString().trim()
            if (playlistName.isBlank()) {
                binding.edtPlaylistName.error =
                    requireContext().getString(R.string.edt_playlist_name_error)
            } else {
                logCreatePlaylistEvent(playlistName)
                onCreatePlaylist(playlistName)
                dismissNow()
            }
        }

        binding.close.setOnClickListener {
            dismissNow()
        }

        return binding.root
    }

    private fun logCreatePlaylistEvent(playlistName: String) {
        analytics.logEvent(
            Constants.CREATE_PLAYLIST_EVENT,
            mapOf(
                Constants.PLAYLIST_NAME to playlistName
            )
        )
    }
}
