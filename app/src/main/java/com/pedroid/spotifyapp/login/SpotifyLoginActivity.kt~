package com.pedroid.spotifyapp.login

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pedroid.common.utils.ClickUtil
import com.pedroid.common.livedata.EventObserver
import com.pedroid.spotifyapp.MainActivity
import com.pedroid.spotifyapp.R
import com.pedroid.spotifyapp.databinding.ActivityLoginBinding
import com.spotify.sdk.android.auth.AuthorizationClient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 4343
    }

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.loginBtn.setOnClickListener {
            if (ClickUtil.isFastDoubleClick) return@setOnClickListener
            viewModel.loginButtonClick()
        }
    }

    private fun setupObservers() {
        viewModel.launchAuthenticationLiveData.observe(
            this,
            EventObserver {
                AuthorizationClient.openLoginActivity(this, REQUEST_CODE, it)
            }
        )
        viewModel.errorLiveData.observe(
            this,
            EventObserver {
                Toast.makeText(this, it.asString(this), Toast.LENGTH_SHORT).show() // TODO Handle error
            }
        )
        viewModel.loginSuccessLiveData.observe(
            this,
            EventObserver {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        if (requestCode == REQUEST_CODE) {
            viewModel.handleAuthResponse(data, resultCode)
        }
    }
}
