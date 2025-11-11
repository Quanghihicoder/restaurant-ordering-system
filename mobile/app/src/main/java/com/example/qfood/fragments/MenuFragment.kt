package com.example.qfood.fragments

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qfood.R
import com.example.qfood.activities.FoodDetailActivity
import com.example.qfood.activities.MainActivity
import com.example.qfood.adapters.MenuAdapter
import com.example.qfood.interfaces.RecyclerViewInterface
import com.example.qfood.interfaces.RecyclerViewInterfaceAnimation
import com.example.qfood.item.Food


class MenuFragment : Fragment() {

    private val screenName = "Menu Screen"

    private var foodSearch = ""
    var foodCate = Int.MIN_VALUE

    private var currentMenuItems = mutableListOf<Food>()
    private var fullMenuItems = mutableListOf<Food>()
    private var categoryList = mutableListOf<Food>()

    private lateinit var adapterMenu: MenuAdapter
    private lateinit var adapterCate: MenuAdapter

    private lateinit var menuSearch: EditText
    private lateinit var searchField: EditText
    private lateinit var rvCategory: RecyclerView
    private lateinit var rvMenu: RecyclerView

    private lateinit var searchNotFound: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(screenName, "On Create")

        // get if passed data
        arguments?.apply {
            foodSearch = getString("MENU_OPTION_SEARCH", "")
            foodCate = getInt("MENU_OPTION_CATEGORY", Int.MIN_VALUE)
        }

        // get full menu
        updateFullMenu((context as MainActivity).readMenuInPref())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(screenName, "On Create View")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        // food datail screen
        val foodDetailScreen = Intent(context, FoodDetailActivity::class.java)

        // init
        menuSearch = view.findViewById(R.id.menuSearch)
        searchField = view.findViewById(R.id.menuSearch)
        rvCategory = view.findViewById(R.id.rvType)
        rvMenu = view.findViewById(R.id.rvMenu)

        searchNotFound = view.findViewById(R.id.notFound)
        searchNotFound.setImageDrawable((context as MainActivity).getImage("cart/notfound.png"))

        // set categories
        categoryList.add(Food(food_name = "Tacos", food_src = "home/taco.png"))
        categoryList.add(Food(food_name = "Burritos", food_src = "home/burrito.png"))
        categoryList.add(Food(food_name = "Nachos", food_src = "home/nachos.png"))
        categoryList.add(Food(food_name = "Sides", food_src = "home/salad.png"))
        categoryList.add(Food(food_name = "Desserts", food_src = "home/dessert.png"))
        categoryList.add(Food(food_name = "Drinks", food_src = "home/coca.png"))
        adapterCate = MenuAdapter(categoryList, 1, context, this)
        adapterCate.setOnCateClickListener(object : RecyclerViewInterface {
            override fun onItemClick(position: Int) {
                if (foodCate == position) {
                    foodCate = Int.MIN_VALUE
                } else {
                    foodCate = position
                }

                handleSearch(foodCate, categoryList, searchField.text.toString(), fullMenuItems)

                adapterCate.notifyDataSetChanged()
            }
        })
        rvCategory.adapter = adapterCate
        rvCategory.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        // set menu
        adapterMenu = MenuAdapter(currentMenuItems, 2, context, this)
        adapterMenu.setOnFoodClickListener(object : RecyclerViewInterfaceAnimation {
            override fun onItemClick(position: Int, imageView: ImageView) {
                foodDetailScreen.putExtra("SelectedFood", currentMenuItems[position])
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    context as MainActivity,
                    imageView,
                    "menu_transition"
                )

                startActivity(foodDetailScreen, options.toBundle())
            }
        })
        rvMenu.adapter = adapterMenu
        rvMenu.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        // set search
        searchField.doAfterTextChanged {
            handleSearch(foodCate, categoryList, searchField.text.toString(), fullMenuItems)
        }

        // init
        init()
        return view
    }

    // search function
    fun handleSearch(
        cateIndex: Int,
        cateList: MutableList<Food>,
        search: String,
        menu: MutableList<Food>
    ) {
        var lists = menu

        if (cateIndex >= 0) {
            lists = lists.filter { item ->
                cateList[cateIndex].food_name.lowercase().contains(item.food_category!!.lowercase())
            }.toMutableList()
        }

        if (!search.isEmpty()) {
            lists = lists.filter { item -> item.food_name.lowercase().contains(search.lowercase()) }
                .toMutableList()
        }

        updateCurrentMenu(lists)
    }

    fun init() {
        menuSearch.setText(foodSearch)
        handleSearch(foodCate, categoryList, searchField.text.toString(), fullMenuItems)
    }

    fun updateCurrentMenu(newList: MutableList<Food>) {
        currentMenuItems.clear()
        currentMenuItems.addAll(newList)

        if (currentMenuItems.size == 0) {
            rvMenu.visibility = View.GONE
            searchNotFound.visibility = View.VISIBLE
        } else {
            rvMenu.visibility = View.VISIBLE
            searchNotFound.visibility = View.GONE
        }

        adapterMenu.notifyDataSetChanged()
    }

    fun updateFullMenu(newList: MutableList<Food>) {
        fullMenuItems.clear()
        fullMenuItems.addAll(newList)
    }
}