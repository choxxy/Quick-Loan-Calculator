package tech.garageprojects.loancalculator.di

import dagger.Binds
import dagger.Module
import tech.garageprojects.loancalculator.service.AbstractCalculator
import tech.garageprojects.loancalculator.service.AnnuityCalculator
import tech.garageprojects.loancalculator.service.DifferentiatedCalculator

@Module
abstract  class CalculatorModule {

    @Binds
    abstract fun providesAnnuityCalculator(annuityCalculator: AnnuityCalculator): AbstractCalculator

    @Binds
    abstract fun providesDifferentiatedCalculator( differentiatedCalculator: DifferentiatedCalculator): AbstractCalculator

}