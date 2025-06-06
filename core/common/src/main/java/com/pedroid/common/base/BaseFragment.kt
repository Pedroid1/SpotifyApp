package com.pedroid.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.pedroid.analytics.Constants
import com.pedroid.analytics.IAnalyticsEventLogger
import javax.inject.Inject

abstract class BaseFragment<VDB : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : Fragment() {

    @Inject
    lateinit var analytics: IAnalyticsEventLogger


    @Suppress("VariableNaming")
    protected lateinit var _binding: VDB

    open val screenName: String
        get() = this::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = (DataBindingUtil.inflate(inflater, layoutId, container, false) as VDB).apply {
        lifecycleOwner = viewLifecycleOwner
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialWork()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        analytics.logEvent(
            Constants.SCREEN_VIEW,
            mapOf(
                Constants.FRAGMENT_NAME to screenName
            )
        )
    }

    abstract fun initialWork()

    abstract fun setupObservers()
}
