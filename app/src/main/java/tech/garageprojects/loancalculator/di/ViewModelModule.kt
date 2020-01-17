package tech.garageprojects.loancalculator.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import tech.garageprojects.loancalculator.CalculatorViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CalculatorViewModel::class)
    abstract fun bindCalcViewModel(calculatorViewModel: CalculatorViewModel): ViewModel
}