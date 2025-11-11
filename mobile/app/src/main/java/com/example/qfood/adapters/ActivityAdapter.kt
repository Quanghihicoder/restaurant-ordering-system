package com.example.qfood.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qfood.R
import com.example.qfood.activities.MainActivity
import com.example.qfood.interfaces.RecyclerViewInterface
import com.example.qfood.item.BillStatus

class ActivityAdapter(val lists: List<BillStatus>, val identify: Int, val context: Context?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var statusList =
        listOf<String>("Cancel", "Confirmed", "Preparing", "Checking", "Delivering", "Delivered")

    inner class ActivityItemViewHolder(itemView: View, listener: RecyclerViewInterface) :
        RecyclerView.ViewHolder(itemView) {
        val billId: TextView
        val billPaid: TextView
        val billAddress: TextView
        val billStatus: TextView
        val billPhone: TextView
        val billWhen: TextView
        val billTotal: TextView

        val confirmContainer: LinearLayout
        val prepareLink: View
        val prepareContainer: LinearLayout
        val checkLink: View
        val checkContainer: LinearLayout
        val deliveryLink: View
        val deliveryContainer: LinearLayout
        val homeLink: View
        val homeContainer: LinearLayout

        init {
            // order info
            billId = itemView.findViewById(R.id.billId)
            billPaid = itemView.findViewById(R.id.billPaid)
            billAddress = itemView.findViewById(R.id.billAddress)
            billStatus = itemView.findViewById(R.id.billStatus)
            billPhone = itemView.findViewById(R.id.billPhone)
            billWhen = itemView.findViewById(R.id.billWhen)
            billTotal = itemView.findViewById(R.id.billTotal)

            // order progress bar
            confirmContainer = itemView.findViewById(R.id.confirmContainer)
            prepareLink = itemView.findViewById(R.id.prepareLink)
            prepareContainer = itemView.findViewById(R.id.prepareContainer)
            checkLink = itemView.findViewById(R.id.checkLink)
            checkContainer = itemView.findViewById(R.id.checkContainer)
            deliveryLink = itemView.findViewById(R.id.deliveryLink)
            deliveryContainer = itemView.findViewById(R.id.deliveryContainer)
            homeLink = itemView.findViewById(R.id.homeLink)
            homeContainer = itemView.findViewById(R.id.homeContainer)

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    lateinit var mListener: RecyclerViewInterface

    fun setOnItemClickListener(listener: RecyclerViewInterface) {
        mListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return identify
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            1 -> return ActivityItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false),
                mListener
            )
            else -> return ActivityItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false),
                mListener
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ActivityItemViewHolder) {
            val animation: Animation =
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_up)

            holder.apply {

                // set info
                billId.text = lists[position].bill_id.toString()
                billPaid.text = lists[position].bill_paid
                billAddress.text = lists[position].bill_address
                billStatus.text = statusList[lists[position].bill_status]
                billPhone.text = lists[position].bill_phone
                billWhen.text = lists[position].bill_when
                billTotal.text = "$" + lists[position].bill_total.toString()

                // set progress bar
                confirmContainer.setBackgroundColor((context as MainActivity).resources.getColor(if (lists[position].bill_status > 0) R.color.greenMain else R.color.grayText))
                prepareLink.setBackgroundColor((context as MainActivity).resources.getColor(if (lists[position].bill_status > 1) R.color.greenMain else R.color.grayText))
                prepareContainer.setBackgroundColor((context as MainActivity).resources.getColor(if (lists[position].bill_status > 1) R.color.greenMain else R.color.grayText))
                checkLink.setBackgroundColor((context as MainActivity).resources.getColor(if (lists[position].bill_status > 2) R.color.greenMain else R.color.grayText))
                checkContainer.setBackgroundColor((context as MainActivity).resources.getColor(if (lists[position].bill_status > 2) R.color.greenMain else R.color.grayText))
                deliveryLink.setBackgroundColor((context as MainActivity).resources.getColor(if (lists[position].bill_status > 3) R.color.greenMain else R.color.grayText))
                deliveryContainer.setBackgroundColor((context as MainActivity).resources.getColor(if (lists[position].bill_status > 3) R.color.greenMain else R.color.grayText))
                homeLink.setBackgroundColor((context as MainActivity).resources.getColor(if (lists[position].bill_status > 4) R.color.greenMain else R.color.grayText))
                homeContainer.setBackgroundColor((context as MainActivity).resources.getColor(if (lists[position].bill_status > 4) R.color.greenMain else R.color.grayText))

                itemView.startAnimation(animation)
            }
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }
}