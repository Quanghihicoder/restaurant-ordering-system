package com.example.qfood.activities

import android.content.Context
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.qfood.R
import com.example.qfood.apis.ServiceBuilder
import com.example.qfood.apis.ServiceInterface
import com.example.qfood.item.CartItem
import com.example.qfood.item.Food
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await
import java.io.InputStream

class FoodDetailActivity : AppCompatActivity() {

    private val screenName = "Food Detail Screen"

    private lateinit var backBtn: ImageButton
    private lateinit var detailImage: ImageView
    private lateinit var detailName: TextView
    private lateinit var detailRate: TextView
    private lateinit var detailNumber: TextView
    private lateinit var detailPrice: TextView
    private lateinit var detailDis: TextView
    private lateinit var detailMinus: ImageButton
    private lateinit var detailPlus: ImageButton
    private lateinit var detailQty: TextView
    private lateinit var detailDesc: TextView
    private lateinit var detailAdd: CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)

        Log.i(screenName, "On Create")

        // init
        backBtn = findViewById<ImageButton>(R.id.foodDetailBack)
        detailImage = findViewById<ImageView>(R.id.foodDetailImage)
        detailName = findViewById<TextView>(R.id.foodDetailName)
        detailRate = findViewById<TextView>(R.id.foodDetailRate)
        detailNumber = findViewById<TextView>(R.id.foodDetailNumber)
        detailPrice = findViewById<TextView>(R.id.foodDetailPrice)
        detailDis = findViewById<TextView>(R.id.foodDetailDiscount)
        detailMinus = findViewById<ImageButton>(R.id.foodDetailMinus)
        detailPlus = findViewById<ImageButton>(R.id.foodDetailPlus)
        detailQty = findViewById<TextView>(R.id.foodDetailQty)
        detailDesc = findViewById<TextView>(R.id.foodDetailDesc)
        detailAdd = findViewById<CardView>(R.id.foodDetailAdd)

        // get selected food
        var food = intent.getParcelableExtra<Food>("SelectedFood")

        // get user id
        val sharedPreferences = getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        val user_id = sharedPreferences.getInt("USER_ID", -1)

        // base qty
        var qty = 1

        // set selected food info
        food?.let {
            detailImage.setImageDrawable(getImage(it.food_src))
            detailName.text = it.food_name
            detailRate.text = it.food_star
            detailNumber.text = "(" + it.food_vote.toString() + "+)"
            detailPrice.text = it.calcPrice().toString()
            if (it.food_discount!!.toDouble() != 0.00) {
                detailDis.text = "$" + it.food_price
                detailDis.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
            } else {
                detailDis.visibility = View.GONE
            }
            detailDesc.text = it.food_desc

            setQty(qty)
        }

        // onclick back
        backBtn.setOnClickListener(View.OnClickListener { view ->
            finish()
        })

        // onclick add
        detailAdd.setOnClickListener(View.OnClickListener { view ->
            if (qty == 0) {
                finish()
            } else {
                handleAdd(user_id, food!!.food_id, qty)
            }
        })

        // onclick minus
        detailMinus.setOnClickListener(View.OnClickListener { view ->
            if (qty > 0) {
                qty = qty - 1
            }
            setQty(qty)
        })

        // onclick plus
        detailPlus.setOnClickListener(View.OnClickListener { view ->
            if (qty < 99) {
                qty = qty + 1
            }
            setQty(qty)
        })
    }


    fun handleAdd(user_id: Int, food_id: Int, qty: Int) {
        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.i(screenName, "Call get cart item")

                val response = retrofit.getCartItem(user_id, food_id)
                if (response.isSuccessful) {
                    Log.i(screenName, "Success call get cart item")
                    val responseBody = response.body()!!

                    // if exist food in cart then call update API instead
                    if (responseBody.size == 0) {
                        Log.i(screenName, "Call post cart item")
                        GlobalScope.launch(Dispatchers.IO) {
                            retrofit.createCartItem(
                                CartItem(user_id, food_id, qty)
                            ).await()
                        }
                    } else {
                        Log.i(screenName, "Call put cart item")
                        GlobalScope.launch(Dispatchers.IO) {
                            retrofit.updateCartItem(
                                CartItem(user_id, food_id, qty + responseBody[0].item_qty)
                            ).await()
                        }
                    }
                    // navigate back
                    finish()
                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun getImage(src: String): Drawable? {
        val imageSrc: InputStream = assets.open(src)
        val d = Drawable.createFromStream(imageSrc, null)
        return d
    }

    fun setQty(qty: Int) {
        if (qty < 10) {
            detailQty.text = "0" + qty.toString()
        } else {
            detailQty.text = qty.toString()
        }
    }
}