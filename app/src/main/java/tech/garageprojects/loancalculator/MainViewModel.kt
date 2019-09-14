package tech.garageprojects.loancalculator

import androidx.lifecycle.ViewModel
import tech.garageprojects.loancalculator.model.Payment

class MainViewModel : ViewModel(){
    var payments: List<Payment>  = arrayListOf()
}
