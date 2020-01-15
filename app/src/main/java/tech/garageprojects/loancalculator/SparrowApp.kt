package tech.garageprojects.loancalculator

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import tech.garageprojects.loancalculator.di.AppComponent
import tech.garageprojects.loancalculator.di.DaggerAppComponent

open class SparrowApp: MultiDexApplication(){

    val appComponent: AppComponent by lazy{
        DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
