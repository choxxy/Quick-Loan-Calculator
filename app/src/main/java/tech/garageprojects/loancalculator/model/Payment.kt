package tech.garageprojects.loancalculator.model

import java.io.Serializable
import java.math.BigDecimal

/**
 * @author Andrei Samkov
 */
class Payment : Serializable {
    var extraPayment = BigDecimal.ZERO
    var date: String = ""
    var nr: Int? = 0
    var balance = BigDecimal.ZERO
    var principal = BigDecimal.ZERO
    var interest = BigDecimal.ZERO
    var amount = BigDecimal.ZERO
    var commission = BigDecimal.ZERO

    constructor(nr: Int?, balance: BigDecimal, principal: BigDecimal, interest: BigDecimal, amount: BigDecimal, date: String
                , extraPayment: BigDecimal) {
        this.nr = nr
        this.balance = balance
        this.principal = principal
        this.interest = interest
        this.amount = amount
        this.date = date
        this.extraPayment = extraPayment
    }

    constructor() {}

    companion object {
        private const val serialVersionUID = 1L
    }
}