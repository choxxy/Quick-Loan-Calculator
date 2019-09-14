package tech.garageprojects.loancalculator.service

import tech.garageprojects.loancalculator.model.Loan
import tech.garageprojects.loancalculator.model.Payment
import java.math.BigDecimal
import java.util.*

/**A differentiated payments your payment every month will decrease because the debt will be
 *  extinguished in equal instalments and interest will accrue monthly on the balance of the debt.
 */
class DifferentiatedCalculator : AbstractCalculator() {

    override fun calculate(loan: Loan) {
        val amount = calculateAmountWithDownPayment(loan)
        loan.residuePayment = getResiduePayment(loan)
        val hasResidue = loan.residuePayment!!.compareTo(BigDecimal.ZERO) > 0
        addDisposableCommission(loan, amount)

        val interestMonthly = loan.interest.divide(BigDecimal("1200"), Calculator.SCALE, Calculator.MODE)
        val period = if (hasResidue) loan.period!! - 1 else loan.period
        val residueInterest = if (hasResidue) loan.residuePayment!!.multiply(interestMonthly) else BigDecimal.ZERO

        val monthlyAmount = amount.subtract(loan.residuePayment).divide(BigDecimal(period!!), Calculator.SCALE, Calculator.MODE)
        var currentAmount = amount
        val payments = ArrayList<Payment>()
        var i = 0
        val calendar = Calendar.getInstance()
        calendar.time = loan.startDate
        while (i < period) {
            val interest = currentAmount.multiply(interestMonthly)
            val payment = interest.add(monthlyAmount)
            currentAmount = currentAmount.subtract(monthlyAmount)

            val p = Payment()
            p.nr = i + 1
            p.interest = interest
            p.principal = monthlyAmount
            p.balance = currentAmount.abs()
            p.date = simpleDateFormat.format(calendar.time)
            calendar.add(Calendar.MONTH, 1)

            addPaymentWithCommission(loan, p, payment)

            payments.add(p)


            i++
        }
        if (hasResidue) {
            val payment = loan.residuePayment
            val p = Payment()
            p.nr = i + 1
            p.balance = currentAmount
            p.interest = residueInterest
            p.principal = payment

            addPaymentWithCommission(loan, p, payment!!.add(residueInterest))

            payments.add(p)
            currentAmount = currentAmount.subtract(payment)

        }
        loan.setPayments(payments)
        loan.effectiveInterestRate = calculateEffectiveInterestRate(loan)
    }

    companion object {
        private val TWO = BigDecimal(2)
    }


}