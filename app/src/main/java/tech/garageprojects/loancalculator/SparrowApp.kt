package tech.garageprojects.loancalculator

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class SparrowApp: MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
