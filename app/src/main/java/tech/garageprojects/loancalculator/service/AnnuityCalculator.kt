package tech.garageprojects.loancalculator.service

import tech.garageprojects.loancalculator.model.Loan
import tech.garageprojects.loancalculator.model.Payment
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.util.*
import javax.inject.Inject

/**
 * Annuity payment remains unchanged throughout the duration of the credit agreement.
 * This means that each month you will pay for the loan by equal installments,
 * which consist of accrued interest per loan and part in principal.
 */


class AnnuityCalculator @Inject constructor(): AbstractCalculator() {


    override fun calculate(loan: Loan) {
        val amount = calculateAmountWithDownPayment(loan)
        loan.residuePayment = getResiduePayment(loan)
        addDisposableCommission(loan, amount)

        val hasResidue = loan.residuePayment!!.compareTo(BigDecimal.ZERO) > 0


        val interestMonthly = loan.interest.divide(BigDecimal("1200"), Calculator.SCALE, Calculator.MODE)
        val oneAndInterest = ONE.add(interestMonthly)
        val period = if (hasResidue) loan.period!! - 1 else loan.period

        val powered = ONE.divide(oneAndInterest.pow(period!!), Calculator.SCALE, Calculator.MODE)
        val divider = ONE.subtract(powered)
        val price = if (hasResidue) amount.subtract(loan.residuePayment) else amount
        var payment: BigDecimal? = price.multiply(interestMonthly).divide(divider, Calculator.SCALE, Calculator.MODE)

        var balance = amount
        val payments = ArrayList<Payment>()
        val residueInterest = if (hasResidue) loan.residuePayment!!.multiply(interestMonthly) else BigDecimal.ZERO
        payment = payment!!.add(residueInterest)
        var i = 0
        val calendar = Calendar.getInstance()
        calendar.time = loan.startDate
        while (i < period) {
            val interest = balance.multiply(interestMonthly)
            val principal = payment!!.subtract(interest)
            balance = balance.subtract(principal)
            val p = Payment()
            p.nr = i + 1
            p.balance = balance.abs()
            p.interest = interest
            p.principal = principal
            p.date = simpleDateFormat.format(calendar.time)
            calendar.add(Calendar.MONTH, 1)

            addPaymentWithCommission(loan, p, payment)

            payments.add(p)

            i++
        }
        if (hasResidue) {
            payment = loan.residuePayment
            val principal = payment
            val p = Payment()
            p.nr = i + 1
            p.balance = balance
            p.interest = residueInterest
            p.principal = principal

            addPaymentWithCommission(loan, p, payment!!.add(residueInterest))

            payments.add(p)
            balance = balance.subtract(principal)

        }
        loan.setPayments(payments)
        loan.effectiveInterestRate = calculateEffectiveInterestRate(loan)
    }

    companion object {


        @JvmStatic
        fun main(args: Array<String>) {
            val calculator = AnnuityCalculator()
            var eff = calculator.calcEffRateUsingIterativeApproach(1.0, 1000.0, doubleArrayOf(600.0, 600.0), 12)
            println((eff * 100).toString() + "%")// Should by ~ 13.07%

            eff = calculator.calcEffRateUsingIterativeApproach(1.0, 1000.0, doubleArrayOf(1200.0), 18)
            println((eff * 100).toString() + "%") // Should by ~ 12.92%
        }
    }


}