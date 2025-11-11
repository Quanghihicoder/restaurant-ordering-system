package com.example.qfood.adapters.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.qfood.R
import com.example.qfood.interfaces.RecyclerViewInterface
import com.example.qfood.item.BillStatus

class AdminAdapter(val lists: List<BillStatus>, val identify: Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var statusList = listOf<String>(
        "Cancel",
        "Confirmed",
        "Preparing",
        "Checking",
        "Delivering",
        "Delivered",
        "Completed"
    )

    inner class AdminItemViewHolder(
        itemView: View,
        dangerListener: RecyclerViewInterface,
        processListener: RecyclerViewInterface
    ) : RecyclerView.ViewHolder(itemView) {
        val billId: TextView
        val userId: TextView
        val billPaid: TextView
        val billAddress: TextView
        val billStatus: TextView
        val billPhone: TextView
        val billWhen: TextView
        val billTotal: TextView

        val dangerText: TextView
        val processText: TextView

        val dangerButton: CardView
        val processButton: CardView

        init {
            // order info
            billId = itemView.findViewById(R.id.billId)
            userId = itemView.findViewById(R.id.userId)
            billPaid = itemView.findViewById(R.id.billPaid)
            billAddress = itemView.findViewById(R.id.billAddress)
            billStatus = itemView.findViewById(R.id.billStatus)
            billPhone = itemView.findViewById(R.id.billPhone)
            billWhen = itemView.findViewById(R.id.billWhen)
            billTotal = itemView.findViewById(R.id.billTotal)

            dangerText = itemView.findViewById(R.id.dangerText)
            processText = itemView.findViewById(R.id.processText)

            dangerButton = itemView.findViewById(R.id.dangerButton)
            processButton = itemView.findViewById(R.id.processButton)

            dangerButton.setOnClickListener {
                dangerListener.onItemClick(adapterPosition)
            }

            processButton.setOnClickListener {
                processListener.onItemClick(adapterPosition)
            }
        }
    }

    lateinit var dangerListener: RecyclerViewInterface
    lateinit var processListener: RecyclerViewInterface

    fun setOnDangerClickListener(listener: RecyclerViewInterface) {
        dangerListener = listener
    }

    fun setOnProcessClickListener(listener: RecyclerViewInterface) {
        processListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return identify
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            1 -> return AdminItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_admin, parent, false),
                dangerListener,
                processListener
            )
            else -> return AdminItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_admin, parent, false),
                dangerListener,
                processListener
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AdminItemViewHolder) {
            holder.apply {
                // set info
                billId.text = lists[position].bill_id.toString()
                userId.text = lists[position].user_id.toString()
                billPaid.text = lists[position].bill_paid
                billAddress.text = lists[position].bill_address
                billStatus.text = statusList[lists[position].bill_status]
                billPhone.text = lists[position].bill_phone
                billWhen.text = lists[position].bill_when
                billTotal.text = "$" + lists[position].bill_total.toString()

                if (lists[position].bill_status < 6) {
                    processText.text = statusList[lists[position].bill_status + 1]
                }

                if (lists[position].bill_status > 1) {
                    dangerButton.visibility = View.GONE
                } else {
                    dangerButton.visibility = View.VISIBLE
                }

                if (lists[position].bill_status == 5 && lists[position].bill_paid == "false") {
                    processButton.visibility = View.GONE
                    dangerButton.visibility = View.VISIBLE
                    dangerText.text = "Paid"
                } else {
                    processButton.visibility = View.VISIBLE
                    dangerButton.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }
}