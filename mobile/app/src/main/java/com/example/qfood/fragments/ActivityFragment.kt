package com.example.qfood.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qfood.R
import com.example.qfood.activities.MainActivity
import com.example.qfood.adapters.ActivityAdapter
import com.example.qfood.apis.ServiceBuilder
import com.example.qfood.apis.ServiceInterface
import com.example.qfood.interfaces.RecyclerViewInterface
import com.example.qfood.item.BillStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ActivityFragment : Fragment() {
    private val screenName = "Activity Screen"

    private var user_id = 0
    private var allBills = mutableListOf<BillStatus>()
    private lateinit var adapterActivity: ActivityAdapter

    private lateinit var activityTitle: TextView
    private lateinit var noOrderImage: ImageView
    private lateinit var rvActivity: RecyclerView

    private lateinit var refreshLayout: SwipeRefreshLayout

    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(screenName, "On Create")

        // get user id
        val sharedPreferences =
            (context as MainActivity).getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        user_id = sharedPreferences.getInt("USER_ID", -1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(screenName, "On Create View")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_activity, container, false)

        // init
        activityTitle = view.findViewById(R.id.activityTitle)

        noOrderImage = view.findViewById(R.id.noOrderImage)
        noOrderImage.setImageDrawable((context as MainActivity).getImage("activity/no-orders.png"))

        // activity recycler view
        rvActivity = view.findViewById<RecyclerView>(R.id.rvActivity)
        adapterActivity = ActivityAdapter(allBills, 1, context)
        adapterActivity.setOnItemClickListener(object : RecyclerViewInterface {
            override fun onItemClick(position: Int) {

            }
        })
        rvActivity.adapter = adapterActivity
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        rvActivity.layoutManager = linearLayoutManager

        // on refresh
        refreshLayout = view.findViewById(R.id.activityRefresh)
        refreshLayout.setOnRefreshListener {
            getAllBills(user_id)
            refreshLayout.isRefreshing = false
        }

        // get all orders
        getAllBills(user_id)

        return view
    }

    fun getAllBills(user_id: Int) {
        Log.i(screenName, "Call get all API bills")

        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = retrofit.getAllBill(user_id)
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    (context as MainActivity).runOnUiThread(Runnable {
                        updateBills((responseBody.filter { it.bill_status in 1..5 }).toMutableList())
                    })
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
    }


    fun updateBills(newList: MutableList<BillStatus>) {
        allBills.clear()
        allBills.addAll(newList)

        if (allBills.size == 0) {
            rvActivity.visibility = View.GONE
            noOrderImage.visibility = View.VISIBLE
            activityTitle.text = "You do not have any orders yet"
        } else {
            rvActivity.visibility = View.VISIBLE
            noOrderImage.visibility = View.GONE
            activityTitle.text = "Your orders will arrive soon"
        }

        adapterActivity.notifyDataSetChanged()
    }
}