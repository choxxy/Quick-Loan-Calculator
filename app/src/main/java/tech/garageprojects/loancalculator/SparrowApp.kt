package tech.garageprojects.loancalculator

import android.app.Application
import android.content.Context
import tech.garageprojects.loancalculator.di.AppComponent
import tech.garageprojects.loancalculator.di.DaggerAppComponent

open class SparrowApp : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }
}

val Context.appComponent: AppComponent
    get() = (this.applicationContext as SparrowApp).appComponent