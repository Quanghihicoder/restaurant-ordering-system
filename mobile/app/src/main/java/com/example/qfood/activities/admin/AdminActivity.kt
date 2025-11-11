package com.example.qfood.activities.admin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qfood.R
import com.example.qfood.adapters.admin.AdminAdapter
import com.example.qfood.apis.ServiceBuilder
import com.example.qfood.apis.ServiceInterface
import com.example.qfood.interfaces.RecyclerViewInterface
import com.example.qfood.item.BillStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await

class AdminActivity : AppCompatActivity() {

    private val screenName = "Activity Screen"

    private var allBills = mutableListOf<BillStatus>()
    private lateinit var adapterAdmin: AdminAdapter
    private lateinit var rvAdmin: RecyclerView

    private lateinit var signoutBtn: CardView
    private lateinit var refreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        Log.i(screenName, "On Create")

        signoutBtn = findViewById(R.id.adminSignOut)
        signoutBtn.setOnClickListener(View.OnClickListener { view ->
            logout()
        })

        refreshLayout = findViewById(R.id.adminRefresh)
        refreshLayout.setOnRefreshListener {
            getAllBills(this)
            refreshLayout.isRefreshing = false
        }

        rvAdmin = findViewById(R.id.rvAdmin)
        adapterAdmin = AdminAdapter(allBills, 1)

        // onclick danger
        adapterAdmin.setOnDangerClickListener(object : RecyclerViewInterface {
            override fun onItemClick(position: Int) {
                if (allBills[position].bill_status == 1) {
                    cancelBill(allBills[position].bill_id, position, this@AdminActivity)
                }

                if (allBills[position].bill_status == 5) {
                    paidBill(allBills[position].bill_id, position, this@AdminActivity)
                }
            }
        })

        // onlclick process
        adapterAdmin.setOnProcessClickListener(object : RecyclerViewInterface {
            override fun onItemClick(position: Int) {
                updateBillStatus(allBills[position].bill_id, position, this@AdminActivity)
            }
        })
        rvAdmin.adapter = adapterAdmin
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        rvAdmin.layoutManager = linearLayoutManager


        getAllBills(this)
    }


    fun getAllBills(context: Context) {
        Log.i(screenName, "Call get all API bills")
        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)
        GlobalScope.launch(Dispatchers.IO) {

            try {
                val response = retrofit.getAllBills()
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    (context as AdminActivity).runOnUiThread(Runnable {
                        updateBills((responseBody.filter { it.bill_status in 1..5 }).toMutableList())
                    })
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }

        }
    }

    fun cancelBill(bill_id: Int, position: Int, context: Context) {
        Log.i(screenName, "Call cancel API bill")

        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {

                retrofit.cancelBill(bill_id).await()
                (context as AdminActivity).runOnUiThread(Runnable {
                    handleCancelAction(position)
                })

            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }


        }
    }

    fun handleCancelAction(position: Int) {
        allBills.removeAt(position)
        adapterAdmin.notifyDataSetChanged()
    }

    fun updateBillStatus(bill_id: Int, position: Int, context: Context) {
        Log.i(screenName, "Call update API bill status")

        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                retrofit.updateBillStatus(bill_id).await()
                (context as AdminActivity).runOnUiThread(Runnable {
                    handleNextAction(position)
                })
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun handleNextAction(position: Int) {
        allBills[position].bill_status = allBills[position].bill_status + 1
        if (allBills[position].bill_status == 6) {
            allBills.removeAt(position)
        }
        adapterAdmin.notifyDataSetChanged()
    }


    fun paidBill(bill_id: Int, position: Int, context: Context) {
        Log.i(screenName, "Call paid API bill")

        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.i(screenName, "Call paid API bill")
                retrofit.paidBill(bill_id).await()

                (context as AdminActivity).runOnUiThread(Runnable {
                    handlePaidAction(position)
                })
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun handlePaidAction(position: Int) {
        allBills[position].bill_paid = "true"
        adapterAdmin.notifyDataSetChanged()
    }

    fun updateBills(newList: MutableList<BillStatus>) {
        allBills.clear()
        allBills.addAll(newList)
        adapterAdmin.notifyDataSetChanged()
    }

    fun logout() {
        // delete user in share preferences
        val sharedPreferences = getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            remove("USER_TYPE")
        }.apply()

        finish()
    }
}