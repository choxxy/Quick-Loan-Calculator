package tech.garageprojects.loancalculator


import android.app.DatePickerDialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType.TYPE_NULL
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.main_fragment.*
import tech.garageprojects.loancalculator.model.Loan
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class MainFragment : Fragment(R.layout.main_fragment) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var calculatorType = 0
    private val viewModel by viewModels<CalculatorViewModel> { viewModelFactory }
    private val simpleDateformat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context!!.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rxBindViews()

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
                calculatorType = position
            }

        }

        calculate.setOnClickListener {
            calculate()
        }

        schedule.setOnClickListener {
            calculate()
            val direction = MainFragmentDirections.actionMainFragmentToScheduleFragment()
            NavHostFragment.findNavController(this).navigate(direction)

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

    fun calculate() {

        if (!TextUtils.isEmpty(amount.text.toString())
                && !TextUtils.isEmpty(term.text.toString())
                && !TextUtils.isEmpty(interest.text.toString())) {

           viewModel.calculate(calculatorType
                    , amount.text.toString()
                    , term.text.toString()
                    , interest.text.toString()
                    , startdate.text.toString()).observe(viewLifecycleOwner, androidx.lifecycle.Observer { loan ->
                calculated(loan)
            })


        } else {
            Toast.makeText(context!!, "Fill all the required values", Toast.LENGTH_SHORT).show()
        }
    }

    fun showCalendar() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { _, y, monthOfYear, dayOfMonth ->
            c.set(Calendar.YEAR, y)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            startdate.setText(simpleDateformat.format(c.time))
        }, year, month, day)

        dpd.show()


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
