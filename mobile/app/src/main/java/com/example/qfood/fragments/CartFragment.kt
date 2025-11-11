package com.example.qfood.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qfood.R
import com.example.qfood.activities.CheckoutActivity
import com.example.qfood.activities.MainActivity
import com.example.qfood.activities.ThankActivity
import com.example.qfood.adapters.CartAdapter
import com.example.qfood.apis.ServiceBuilder
import com.example.qfood.apis.ServiceInterface
import com.example.qfood.interfaces.RecyclerViewInterface
import com.example.qfood.item.CartFood
import com.example.qfood.item.CartItem
import com.example.qfood.item.Food
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await


class CartFragment : Fragment() {
    private var screenName = "Cart Screen"

    private var user_id = 0
    private var fullMenuItems = mutableListOf<Food>()
    private var fullCartItems = mutableListOf<CartFood>()

    private lateinit var adapterCart: CartAdapter

    private lateinit var noItemContainer: LinearLayout
    private lateinit var cartItemContainer: LinearLayout

    private lateinit var continueContainer: LinearLayout
    private lateinit var checkOutContainer: LinearLayout

    private lateinit var itemCount: TextView

    private lateinit var cartSummary: TextView
    private lateinit var cartDiscount: TextView
    private lateinit var cartTotal: TextView

    private lateinit var removeAll: CardView
    private lateinit var checkOut: CardView

    private lateinit var imageNoItem: ImageView

    private lateinit var rvCart: RecyclerView
    private lateinit var continueShoppingBtn: CardView

    val CHECK_OUT = 3
    val THANK_YOU = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(screenName, "On Create")

        // get user id
        val sharedPreferences =
            (context as MainActivity).getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        user_id = sharedPreferences.getInt("USER_ID", -1)

        updateFullMenu((context as MainActivity).readMenuInPref())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(screenName, "On Create view")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        // init
        noItemContainer = view.findViewById(R.id.noCartItemContainer)
        cartItemContainer = view.findViewById(R.id.cartItemContainer)
        continueContainer = view.findViewById(R.id.continueContainer)
        checkOutContainer = view.findViewById(R.id.checkOutContainer)
        itemCount = view.findViewById(R.id.itemCount)

        cartSummary = view.findViewById(R.id.cartSummary)
        cartDiscount = view.findViewById(R.id.cartDiscount)
        cartTotal = view.findViewById(R.id.cartTotal)
        removeAll = view.findViewById(R.id.removeAll)
        checkOut = view.findViewById(R.id.checkOut)

        imageNoItem = view.findViewById(R.id.imageNoItem)
        imageNoItem.setImageDrawable((context as MainActivity).getImage("cart/notfound.png"))

        // recycler view cart
        rvCart = view.findViewById(R.id.rvCart)
        adapterCart = CartAdapter(fullCartItems, 1, context)

        // onclick delete
        adapterCart.setOnDeleteClickListener(object : RecyclerViewInterface {
            override fun onItemClick(position: Int) {
                handleDeleteItem(user_id, fullCartItems[position].food_id, position)
            }
        })

        // onclick minus
        adapterCart.setOnMinusClickListener(object : RecyclerViewInterface {
            override fun onItemClick(position: Int) {
                if (fullCartItems[position].item_qty > 1) {
                    handleItemQtyChange(
                        user_id,
                        fullCartItems[position].food_id,
                        fullCartItems[position].item_qty - 1,
                        position
                    )
                }
            }
        })

        // onlclick plus
        adapterCart.setOnPlusClickListener(object : RecyclerViewInterface {
            override fun onItemClick(position: Int) {
                if (fullCartItems[position].item_qty < 99) {
                    handleItemQtyChange(
                        user_id,
                        fullCartItems[position].food_id,
                        fullCartItems[position].item_qty + 1,
                        position
                    )
                }
            }
        })
        rvCart.adapter = adapterCart
        rvCart.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        // continue shopping
        continueShoppingBtn = view.findViewById(R.id.continueShopping)
        continueShoppingBtn.setOnClickListener(View.OnClickListener { view ->
            (context as MainActivity).apply {
                this.changeSelected(R.id.menuScreen)
            }
        })

        // on click remove all
        removeAll.setOnClickListener(View.OnClickListener { view ->
            handleRemoveAll(user_id)
        })

        // onclick check out
        checkOut.setOnClickListener(View.OnClickListener { view ->
            // start check out activity
            val checkOutScreen = Intent(context, CheckoutActivity::class.java)
            val bill = calcBill()
            checkOutScreen.putExtra("FullCartItem", fullCartItems.toList().toTypedArray())
            checkOutScreen.putExtra("CartDiscount", bill[0])
            checkOutScreen.putExtra("CartDelivery", bill[1])
            checkOutScreen.putExtra("CartTotal", bill[2])

            startActivityForResult(checkOutScreen, CHECK_OUT)
        })

        // get all cart
        getALlCartItem(user_id)

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i(screenName, "On Activity Result")

        // if check out successfully then move to thank screen
        if (requestCode === CHECK_OUT) {
            if (resultCode === Activity.RESULT_OK) {
                startActivityForResult(Intent(context, ThankActivity::class.java), THANK_YOU)
            }
        }

        // if check out successfully then move to activity screen
        if (requestCode === THANK_YOU) {
            if (resultCode === Activity.RESULT_OK) {
                (context as MainActivity).apply {
                    this.changeSelected(R.id.activityScreen)
                }
            }
        }
    }

    fun getALlCartItem(user_id: Int) {
        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

        GlobalScope.launch(Dispatchers.IO) {

            try {
                Log.i(screenName, "Call get all cart item")

                val response = retrofit.getCart(user_id)
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    (context as MainActivity).runOnUiThread(Runnable {
                        setCurrentCart(findMatchFood(responseBody, fullMenuItems))
                    })
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }

        }
    }

    fun handleDeleteItem(user_id: Int, food_id: Int, index: Int) {
        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.i(screenName, "Call delete cart item")
                retrofit.deleteCartItem(user_id, food_id).await()

                (context as MainActivity).runOnUiThread(Runnable {
                    // delete on current value
                    fullCartItems.removeAt(index)
                    updateCurrentCart()
                })
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun handleItemQtyChange(user_id: Int, food_id: Int, qty: Int, index: Int) {
        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.i(screenName, "Call update cart item qty")
                retrofit.updateCartItem(
                    CartItem(user_id, food_id, qty)
                ).await()

                (context as MainActivity).runOnUiThread(Runnable {
                    // update on current value
                    fullCartItems[index].item_qty = qty
                    updateCurrentCart()
                })
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun handleRemoveAll(user_id: Int) {
        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.i(screenName, "Call delete all cart item")
                retrofit.deleteCart(
                    user_id
                ).await()

                (context as MainActivity).runOnUiThread(Runnable {
                    // delete on current value
                    fullCartItems.clear()
                    updateCurrentCart()
                })

            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }

        }

    }


    fun updateFullMenu(newList: MutableList<Food>) {
        fullMenuItems.clear()
        fullMenuItems.addAll(newList)
    }

    fun setCurrentCart(newList: MutableList<CartFood>) {
        fullCartItems.clear()
        fullCartItems.addAll(newList)
        updateCurrentCart()
    }

    fun updateCurrentCart() {
        if (fullCartItems.size == 0) {
            noItemContainer.visibility = View.VISIBLE
            cartItemContainer.visibility = View.GONE
            continueContainer.visibility = View.VISIBLE
            checkOutContainer.visibility = View.GONE
            removeAll.visibility = View.GONE
            itemCount.text = "0 Item"
        } else {
            noItemContainer.visibility = View.GONE
            cartItemContainer.visibility = View.VISIBLE
            continueContainer.visibility = View.GONE
            checkOutContainer.visibility = View.VISIBLE
            removeAll.visibility = View.VISIBLE
            if (fullCartItems.size == 1) {
                itemCount.text = fullCartItems.size.toString() + " Item"
            } else {
                itemCount.text = fullCartItems.size.toString() + " Items"
            }
        }

        calcBill()

        adapterCart.notifyDataSetChanged()
    }

    fun calcBill(): Array<Int> {
        var summary = 0.00
        var discount = 0.00

        fullCartItems.forEach {
            summary = summary + it.calcTotal()
            discount = discount + it.calcDiscount()
        }

        cartSummary.text = "$" + summary.toString()
        cartDiscount.text = "$" + discount.toString()
        cartTotal.text = "$" + (summary + 15.00).toString()

        return arrayOf(discount.toInt(), 15, summary.toInt() + 15)
    }

    fun createCartWithFood(item: CartItem, food: Food): CartFood {
        return CartFood(
            item.user_id,
            item.food_id,
            item.item_qty,
            food.food_name,
            food.food_price,
            food.food_discount,
            food.food_desc,
            food.food_src
        )
    }

    fun findMatchFood(
        fullCartItems: MutableList<CartItem>,
        fullMenuItems: MutableList<Food>
    ): MutableList<CartFood> {
        var lists = mutableListOf<CartFood>()
        fullCartItems.forEach {
            lists.add(
                createCartWithFood(
                    it,
                    fullMenuItems.filter { item -> it.food_id == item.food_id }[0]
                )
            )
        }

        return lists
    }
}