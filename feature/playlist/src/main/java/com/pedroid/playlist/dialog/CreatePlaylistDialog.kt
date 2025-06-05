package com.pedroid.playlist.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.pedroid.feature.playlist.R
import com.pedroid.feature.playlist.databinding.DialogCreatePlaylistBinding

class CreatePlaylistDialog(
    private val onCreatePlaylist: (String) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogCreatePlaylistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.pedroid.core.design_system.R.style.OverlayScreenDialogTheme)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            (resources.displayMetrics.heightPixels * 0.9).toInt()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreatePlaylistBinding.inflate(inflater, container, false)

        binding.createPlaylistBtn.setOnClickListener {
            val playlistName = binding.edtPlaylistName.text.toString()
            if (playlistName.isBlank()) {
                binding.edtPlaylistName.error =
                    requireContext().getString(R.string.edt_playlist_name_error)
            } else {
                onCreatePlaylist(playlistName)
                dismissNow()
            }
        }

        binding.close.setOnClickListener {
            dismissNow()
        }

        return binding.root
    }
}
