package tech.garageprojects.loancalculator.service

import tech.garageprojects.loancalculator.model.Loan
import java.math.RoundingMode

interface Calculator {

    fun calculate(loan: Loan)

    companion object {
        @JvmField
        val MODE = RoundingMode.HALF_UP
        @JvmField
        val SCALE = 8
    }
}