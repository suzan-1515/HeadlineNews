package com.cognota.core.util

import android.os.Looper

fun onMainThread() = Looper.myLooper() == Looper.getMainLooper()