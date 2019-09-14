package tech.garageprojects.loancalculator


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import tech.garageprojects.loancalculator.model.Payment
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat

class ScheduleViewAdapter(
        private val mValues: List<Payment>,
        private val mListener: ScheduleFragment.OnListItemInteractionListener?)
    : RecyclerView.Adapter<ScheduleViewAdapter.ViewHolder>() {

    val currencyFormat = DecimalFormat("#,###,##0.00")

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Payment
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListItemInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mNo.text = item.nr.toString()
        holder.mPrincipal.text = currencyFormat.format(item.principal)
        holder.mBalance.text = currencyFormat.format(item.balance)
        if (item.extraPayment == BigDecimal.ZERO)
            holder.mExtraPay.visibility = View.GONE
        else
            holder.mExtraPay.text = currencyFormat.format(item.extraPayment)
        holder.mPayDate.text = item.date
        holder.mPayment.text = currencyFormat.format(item.amount)
        holder.mInterest.text = currencyFormat.format(item.interest)


        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mNo: TextView = mView.number
        val mPrincipal: TextView = mView.principal
        val mExtraPay: TextView = mView.extra_payment
        val mInterest: TextView = mView.interest
        val mBalance: TextView = mView.balance
        val mPayment: TextView = mView.payment
        val mPayDate: TextView = mView.date

    }
}
