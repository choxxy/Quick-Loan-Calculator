package tech.garageprojects.loancalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_schedule_list.*
import kotlinx.android.synthetic.main.fragment_schedule_list.adView
import kotlinx.android.synthetic.main.main_fragment.*
import tech.garageprojects.loancalculator.model.Payment


class ScheduleFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_list, container, false)


        return view
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
        viewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)

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
