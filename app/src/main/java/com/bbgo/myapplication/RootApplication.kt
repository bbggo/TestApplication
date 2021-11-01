package com.bbgo.myapplication

import android.app.Application
import android.content.Context
import com.bbgo.myapplication.hook.HookUtils

class RootApplication : Application() {

    override fun attachBaseContext(base: Context) {
        HookUtils.hookInit()
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        private var context: RootApplication? = null
        fun getContext(): Context {
            return context!!
        }
    }
}