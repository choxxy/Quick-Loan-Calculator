package tech.garageprojects.loancalculator.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import tech.garageprojects.loancalculator.MainFragment
import tech.garageprojects.loancalculator.ScheduleFragment

@Component(modules = [ViewModelModule::class, ViewModelFactoryModule::class, CalculatorModule::class])
interface AppComponent {
    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }


    fun inject(mainFragment: MainFragment)

    fun inject(scheduleFragment: ScheduleFragment)
}