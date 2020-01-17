package tech.garageprojects.loancalculator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_schedule_list.*
import tech.garageprojects.loancalculator.model.Payment
import javax.inject.Inject


class ScheduleFragment : Fragment(R.layout.fragment_schedule_list) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<CalculatorViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context!!.appComponent.inject(this)
    }


    override fun onPause() {
        adView.pause()
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        with(list) {
            adapter = ScheduleViewAdapter(viewModel.payments, object : OnListItemInteractionListener {
                override fun onListItemInteraction(item: Payment) {

                }
            })
        }

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

    }

    interface OnListItemInteractionListener {
        fun onListItemInteraction(item: Payment)
    }

}
