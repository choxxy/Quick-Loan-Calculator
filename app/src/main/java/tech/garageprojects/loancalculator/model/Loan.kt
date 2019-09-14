package tech.garageprojects.loancalculator.model

import java.io.Serializable
import java.math.BigDecimal
import java.util.*

class Loan : Serializable {

    var startDate: Date = Date()
    var isCalculated: Boolean = false
    var loanType = 0
    var amount = BigDecimal.ZERO
    var interest = BigDecimal.ZERO
    var fixedPayment = BigDecimal.ZERO
    var period: Int? = 0
    private var payments: List<Payment> = ArrayList()

    var totalInterests = BigDecimal.ZERO
        private set
    var minMonthlyPayment = BigDecimal.ZERO
        private set
    var maxMonthlyPayment = BigDecimal.ZERO
        private set

    var downPayment: BigDecimal? = null
    var disposableCommission: BigDecimal? = null
    var monthlyCommission: BigDecimal? = null
    var residue: BigDecimal? = null

    var downPaymentType: Int = 0
    var disposableCommissionType: Int = 0
    var monthlyCommissionType: Int = 0
    var residueType: Int = 0

    var downPaymentPayment: BigDecimal? = null
    var disposableCommissionPayment: BigDecimal? = null
    var monthlyCommissionPayment: BigDecimal? = null
    var residuePayment: BigDecimal? = null

    var commissionsTotal: BigDecimal = BigDecimal.ZERO

    var effectiveInterestRate = BigDecimal.ZERO

    val totalAmount: BigDecimal
        get() {
            var total = amount.add(totalInterests)

            if (commissionsTotal != null && commissionsTotal!!.compareTo(BigDecimal.ZERO) != 0) {
                total = total.add(commissionsTotal)
            }
            return total
        }

    fun setPayments(payments: List<Payment>) {
        this.payments = payments
        totalInterests = BigDecimal.ZERO
        minMonthlyPayment = BigDecimal.ZERO
        maxMonthlyPayment = BigDecimal.ZERO
        commissionsTotal = BigDecimal.ZERO

        if (disposableCommissionPayment != null) {
            commissionsTotal = commissionsTotal.add(disposableCommissionPayment)
        }

        for ((i, payment) in payments.withIndex()) {
            totalInterests = totalInterests.add(payment.interest)

            if (payment.commission != null) {
                commissionsTotal = commissionsTotal.add(payment.commission)
            }

            if (i + 1 != payments.size || residue!!.compareTo(BigDecimal.ZERO) == 0) {
                if (minMonthlyPayment == BigDecimal.ZERO) {
                    minMonthlyPayment = payment.amount
                } else {
                    minMonthlyPayment = minMonthlyPayment.min(payment.amount)
                }
                maxMonthlyPayment = maxMonthlyPayment.max(payment.amount)
            }
        }
    }

    fun getPayments(): List<Payment> {
        return ArrayList(payments)
    }

    fun hasDownPayment(): Boolean {
        return downPaymentPayment != null && downPaymentPayment!!.compareTo(BigDecimal.ZERO) > 0
    }

    fun hasDisposableCommission(): Boolean {
        return disposableCommissionPayment != null && disposableCommissionPayment!!.compareTo(BigDecimal.ZERO) > 0
    }

    fun hasAnyCommission(): Boolean {
        return commissionsTotal!!.compareTo(BigDecimal.ZERO) > 0
    }

    companion object {
        private const val serialVersionUID = 1L

        const val PERCENT = 0
        const val VALUE = 1
    }
}