package tech.garageprojects.loancalculator.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: SparrowVMFactory) : ViewModelProvider.Factory
}