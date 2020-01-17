package tech.garageprojects.loancalculator.model

sealed class CalculatorType {
    object AnnuityCalc : CalculatorType()
    object DifferentiatedCalc : CalculatorType()
}