package com.example.qfood.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qfood.R
import com.example.qfood.activities.AccountActivity
import com.example.qfood.activities.FoodDetailActivity
import com.example.qfood.activities.MainActivity
import com.example.qfood.adapters.HomeAdapter
import com.example.qfood.apis.ServiceBuilder
import com.example.qfood.apis.ServiceInterface
import com.example.qfood.interfaces.RecyclerViewInterface
import com.example.qfood.item.Food
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val screenName = "Home Screen"

    private val LAUNCH_ACCOUNT_ACTIVITY = 1

    private lateinit var accountBtn: ImageButton
    private lateinit var searchField: EditText

    private var categoryList = mutableListOf<Food>()
    private lateinit var adapterCate: HomeAdapter

    private var categoryBest = mutableListOf<Food>()
    private lateinit var adapterBest: HomeAdapter

    private var categoryDis = mutableListOf<Food>()
    private lateinit var adapterDis: HomeAdapter

    private lateinit var viewAllBest: TextView
    private lateinit var viewAllDis: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(screenName, "On Create")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(screenName, "On Create View")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // account btn
        accountBtn = view.findViewById(R.id.homeProfileBtn)
        accountBtn.setOnClickListener(View.OnClickListener { view ->
            startActivityForResult(
                Intent(context, AccountActivity::class.java),
                LAUNCH_ACCOUNT_ACTIVITY
            )
        })

        // search bar
        searchField = view.findViewById(R.id.homeSearch)
        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!searchField.text.toString().isEmpty()) {
                    navigateMenu(searchField.text.toString(), null)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        // category
        val rvCategory = view.findViewById<RecyclerView>(R.id.rvCategory)
        categoryList.add(Food(food_name = "Tacos", food_src = "home/taco.png"))
        categoryList.add(Food(food_name = "Burritos", food_src = "home/burrito.png"))
        categoryList.add(Food(food_name = "Nachos", food_src = "home/nachos.png"))
        categoryList.add(Food(food_name = "Sides", food_src = "home/salad.png"))
        categoryList.add(Food(food_name = "Desserts", food_src = "home/dessert.png"))
        categoryList.add(Food(food_name = "Drinks", food_src = "home/coca.png"))
        adapterCate = HomeAdapter(categoryList, 1, context)
        adapterCate.setOnItemClickListener(object : RecyclerViewInterface {
            override fun onItemClick(position: Int) {
                navigateMenu(null, position)
            }
        })
        rvCategory.adapter = adapterCate
        rvCategory.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // best seller
        val rvBest = view.findViewById<RecyclerView>(R.id.rvBest)
        adapterBest = HomeAdapter(categoryBest, 2, context)
        adapterBest.setOnItemClickListener(object : RecyclerViewInterface {
            override fun onItemClick(position: Int) {
                val foodDetailScreen = Intent(context, FoodDetailActivity::class.java)
                foodDetailScreen.putExtra("SelectedFood", categoryBest[position])
                startActivity(foodDetailScreen)
            }
        })
        rvBest.adapter = adapterBest
        rvBest.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // view all best seller
        viewAllBest = view.findViewById(R.id.homeViewAllBest)
        viewAllBest.setOnClickListener(View.OnClickListener { view ->
            navigateMenu(null, null)
        })

        // discount
        val rvDis = view.findViewById<RecyclerView>(R.id.rvDis)
        adapterDis = HomeAdapter(categoryDis, 3, context)
        adapterDis.setOnItemClickListener(object : RecyclerViewInterface {
            override fun onItemClick(position: Int) {
                val foodDetailScreen = Intent(context, FoodDetailActivity::class.java)
                foodDetailScreen.putExtra("SelectedFood", categoryDis[position])
                startActivity(foodDetailScreen)
            }
        })
        rvDis.adapter = adapterDis
        rvDis.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // view all discount
        viewAllDis = view.findViewById(R.id.homeViewALlDis)
        viewAllDis.setOnClickListener(View.OnClickListener { view ->
            navigateMenu(null, null)
        })


        // get all menu
        getMenuItem(context as MainActivity)

        return view
    }

    // receive logout pressed from account activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i(screenName, "On Activity Result")

        // if receive logout from account screen
        if (requestCode === LAUNCH_ACCOUNT_ACTIVITY) {
            if (resultCode === Activity.RESULT_OK) {
                (context as MainActivity).logout()
            }
        }
    }

    // move to menu fragment with param
    fun navigateMenu(search: String?, category: Int?) {
        val bundle = Bundle()

        bundle.apply {
            if (search != null) {
                putString("MENU_OPTION_SEARCH", search)
            }
            if (category != null) {
                putInt("MENU_OPTION_CATEGORY", category)
            }
        }

        val menuScreen = MenuFragment()
        menuScreen.arguments = bundle

        (context as MainActivity).apply {
            this.changeSelected(R.id.menuScreen)
            this.replaceFragment(menuScreen)
        }
    }

    // get menu API
    fun getMenuItem(context: Context) {
        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.i(screenName, "Call get all menu item")

                val response = retrofit.getFoods()
                if (response.isSuccessful) {
                    Log.i(screenName, "Success call get all menu item")

                    val responseBody = response.body()!!

                    // save menu to share preferences
                    writeMenuInPref(context, responseBody)

                    // set curent value
                    (context as MainActivity).runOnUiThread(Runnable {
                        update(responseBody)
                    })

                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }


        }
    }

    // update recycler view
    fun update(newList: MutableList<Food>) {
        categoryBest.clear()
        categoryBest.addAll((newList.sortedByDescending { it.food_vote!!.toInt() }).take(10))
        categoryDis.clear()
        categoryDis.addAll(newList.filter { it.food_discount!!.toDouble() > 0.00 }
            .sortedByDescending { it.food_discount!!.toDouble() }.take(10))
        adapterBest.notifyDataSetChanged()
        adapterDis.notifyDataSetChanged()
    }

    // write menu items as json to share preferences
    fun writeMenuInPref(context: Context, list: MutableList<Food>) {
        val gson = Gson()
        val jsonString: String = gson.toJson(list)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("FOODS", jsonString)
        }.apply()
    }
}