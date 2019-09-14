package tech.garageprojects.loancalculator


import android.app.DatePickerDialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType.TYPE_NULL
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.main_fragment.*
import tech.garageprojects.loancalculator.model.Loan
import tech.garageprojects.loancalculator.service.AnnuityCalculator
import tech.garageprojects.loancalculator.service.Calculator
import tech.garageprojects.loancalculator.service.DifferentiatedCalculator
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.gms.ads.AdRequest




class MainFragment : Fragment() {


    private lateinit var viewModel: MainViewModel
    private var mViewModel: MainViewModel? = null
    private var calculators = arrayOf(AnnuityCalculator(), DifferentiatedCalculator())
    private var calculator: Calculator = AnnuityCalculator()
    private val simpleDateformat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    private val loan = Loan()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rxBindViews()
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
    }

    override fun onPause() {
        adView.pause()
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    private fun rxBindViews() {


        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        loan_type.setSelection(0)
        loan_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                calculator = calculators[position]
            }

        }

        calculate.setOnClickListener {
            calculate()
        }

        schedule.setOnClickListener {
            calculate()
            if (loan.isCalculated) {
                val direction = MainFragmentDirections.actionMainFragmentToScheduleFragment()
                NavHostFragment.findNavController(this).navigate(direction)
            }
        }

        startdate.setOnClickListener {
            showCalendar()
        }

        val cal = Calendar.getInstance()
        startdate.setText(simpleDateformat.format(cal.time))

        startdate.isFocusable = true
        startdate.isFocusableInTouchMode = true
        startdate.inputType = TYPE_NULL

        installments.text = formatText(getString(R.string.monthly_payment, ""))
        total_interest.text = formatText(getString(R.string.total_interest, ""))
        total_loan.text = formatText(getString(R.string.total_loan_amount, ""))
        val calendar = Calendar.getInstance()
        enddate.text = formatText(getString(R.string.end_date, simpleDateformat.format(calendar.time)))
    }

    fun showCalendar() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { view, y, monthOfYear, dayOfMonth ->
            c.set(Calendar.YEAR, y)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            startdate.setText(simpleDateformat.format(c.time))
        }, year, month, day)

        dpd.show()


    }

    fun calculate() {

        if (!TextUtils.isEmpty(amount.text.toString())
                && !TextUtils.isEmpty(term.text.toString())
                && !TextUtils.isEmpty(interest.text.toString())) {


            loan.amount = BigDecimal(amount.text.toString())
            loan.period = term.text.toString().toInt()
            loan.interest = BigDecimal(interest.text.toString())
            loan.downPayment = BigDecimal.ZERO
            loan.disposableCommission = BigDecimal.ZERO
            loan.monthlyCommission = BigDecimal.ZERO
            loan.residue = BigDecimal.ZERO
            loan.startDate = simpleDateformat.parse(startdate.text.toString())!!

            calculator.calculate(loan)
            loan.isCalculated = true

            calculated(loan)

            viewModel.payments = loan.getPayments()

        } else {
            Toast.makeText(context!!, "Fill all the required values", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    fun calculated(loan: Loan) {

        val currencyFormat = DecimalFormat("#,###,##0.00")

        installments.text = formatText(getString(R.string.monthly_payment, currencyFormat.format(loan.maxMonthlyPayment.toFloat())))
        total_interest.text = formatText(getString(R.string.total_interest, currencyFormat.format(loan.totalInterests.toFloat())))
        total_loan.text = formatText(getString(R.string.total_loan_amount, currencyFormat.format(loan.totalAmount.toFloat())))
        val calendar = Calendar.getInstance()
        calendar.time = loan.startDate
        calendar.add(Calendar.MONTH, loan.period!!)
        enddate.text = formatText(getString(R.string.end_date, simpleDateformat.format(calendar.time)))
    }


    fun formatText(text: String): SpannableStringBuilder {
        val ssBuilder = SpannableStringBuilder(text)
        ssBuilder.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                text.indexOf(":"),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return ssBuilder

    }

}
