package com.kvest.roomplayground.ext

import android.content.Context
import com.kvest.roomplayground.PlaygroundApplication

val Context.app : PlaygroundApplication
    get() = applicationContext as PlaygroundApplication