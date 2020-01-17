package tech.garageprojects.loancalculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.garageprojects.loancalculator.model.Loan
import tech.garageprojects.loancalculator.model.Payment
import tech.garageprojects.loancalculator.service.AnnuityCalculator
import tech.garageprojects.loancalculator.service.DifferentiatedCalculator
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CalculatorViewModel @Inject constructor(
        private val annuityCalculator: AnnuityCalculator,
        private val differentiatedCalculator: DifferentiatedCalculator
) : ViewModel() {
    var payments: List<Payment> = arrayListOf()
    private val simpleDateformat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    private val results = MutableLiveData<Loan>()

    fun calculate(calculatorType: Int,
                  amount: String, term: String, interest: String, startDate: String): LiveData<Loan> {


        val calculator = if (calculatorType == 0)
            annuityCalculator else differentiatedCalculator

        var loan = Loan()
        loan.amount = BigDecimal(amount)
        loan.period = term.toInt()
        loan.interest = BigDecimal(interest)
        loan.downPayment = BigDecimal.ZERO
        loan.disposableCommission = BigDecimal.ZERO
        loan.monthlyCommission = BigDecimal.ZERO
        loan.residue = BigDecimal.ZERO
        loan.startDate = simpleDateformat.parse(startDate)!!

        calculator.calculate(loan)
        loan.isCalculated = true

        payments = loan.getPayments()

        results.value = loan

        return results

    }
}
