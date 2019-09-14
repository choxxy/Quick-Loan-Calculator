package tech.garageprojects.loancalculator.service

class Calculation(val installments: String, val totalLoan: String, val totalInterest: String, val endDate: String) {
    constructor() : this("","","","")
}
