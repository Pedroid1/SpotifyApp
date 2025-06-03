package com.pedroid.common

import android.os.SystemClock

object ClickUtil {
    private var mLastClickTime: Long = 0
    private const val SPACE_TIME = 500

    val isFastDoubleClick: Boolean
        get() {
            val time = SystemClock.elapsedRealtime()
            if (time - mLastClickTime <= SPACE_TIME) {
                return true
            } else {
                mLastClickTime = time
                return false
            }
        }
}
