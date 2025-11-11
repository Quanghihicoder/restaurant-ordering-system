package com.example.qfood.activities

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.qfood.R
import com.example.qfood.apis.ServiceBuilder
import com.example.qfood.apis.ServiceInterface
import com.example.qfood.item.BillDetail
import com.example.qfood.item.BillStatus
import com.example.qfood.item.CartFood
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class CheckoutActivity : AppCompatActivity() {

    private val screenName = "Checkout Screen"

    private var cash = true

    private lateinit var backBtn: ImageButton
    private lateinit var checkOutBtn: Button

    private lateinit var checkOutName: TextView
    private lateinit var checkOutTotal: TextView

    private lateinit var checkOutPhone: EditText
    private lateinit var checkOutAddress: EditText

    private lateinit var paymentMethod: RadioGroup

    private lateinit var cardNumber: EditText
    private lateinit var cardName: EditText

    private lateinit var cardExpiryDate: EditText
    private lateinit var cardCvv: EditText

    private lateinit var background1: ImageView
    private lateinit var background2: ImageView
    private lateinit var background3: ImageView
    private lateinit var background4: ImageView
    private lateinit var background5: ImageView
    private lateinit var background6: ImageView
    private lateinit var background7: ImageView

    private var fullCartItems = mutableListOf<CartFood>()
    private var cartDiscount: Int = 0
    private var cartDelivery: Int = 0
    private var cartTotal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        Log.i(screenName, "On Create")

        // get checout info
        fullCartItems = intent.getParcelableArrayExtra("FullCartItem")!!.toList()
            .toMutableList() as MutableList<CartFood>
        cartDiscount = intent.getIntExtra("CartDiscount", 0)
        cartDelivery = intent.getIntExtra("CartDelivery", 0)
        cartTotal = intent.getIntExtra("CartTotal", 0)

        // init
        backBtn = findViewById(R.id.backBtn)
        checkOutBtn = findViewById(R.id.checkOutBtn)

        checkOutName = findViewById(R.id.checkOutName)
        checkOutTotal = findViewById(R.id.checkOutTotal)
        checkOutPhone = findViewById(R.id.checkOutPhone)
        checkOutAddress = findViewById(R.id.checkOutAddress)

        paymentMethod = findViewById(R.id.paymentMethod)

        cardNumber = findViewById(R.id.cardNumber)
        cardName = findViewById(R.id.cardName)
        cardExpiryDate = findViewById(R.id.cardExpiry)
        cardCvv = findViewById(R.id.cardCvv)

        background1 = findViewById(R.id.backgroundImage1)
        background1.setImageDrawable(getImage("checkout/background1.png"))
        background2 = findViewById(R.id.backgroundImage2)
        background2.setImageDrawable(getImage("checkout/background2.png"))
        background3 = findViewById(R.id.backgroundImage3)
        background3.setImageDrawable(getImage("checkout/background3.png"))
        background4 = findViewById(R.id.backgroundImage4)
        background4.setImageDrawable(getImage("checkout/background4.png"))
        background5 = findViewById(R.id.backgroundImage5)
        background5.setImageDrawable(getImage("checkout/background5.png"))
        background6 = findViewById(R.id.backgroundImage6)
        background6.setImageDrawable(getImage("checkout/background6.png"))
        background7 = findViewById(R.id.backgroundImage7)
        background7.setImageDrawable(getImage("checkout/background7.png"))

        // get user name, user phone
        val sharedPreferences = getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        checkOutName.text = sharedPreferences.getString("USER_NAME", "") + "'S ORDER"
        checkOutPhone.setText(sharedPreferences.getString("USER_PHONE", ""))

        // set checkout info
        checkOutTotal.text = "$" + cartTotal.toString()

        // on change payment method
        paymentMethod.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.paymentCash) {
                clearForm()

                cardNumber.visibility = View.GONE
                cardName.visibility = View.GONE
                cardExpiryDate.visibility = View.GONE
                cardCvv.visibility = View.GONE

                cash = true
            }

            if (checkedId == R.id.paymentCard) {
                cardNumber.visibility = View.VISIBLE
                cardName.visibility = View.VISIBLE
                cardExpiryDate.visibility = View.VISIBLE
                cardCvv.visibility = View.VISIBLE

                cash = false
            }
        })

        // force uppercase
        cardName.doAfterTextChanged {
            it.toString().uppercase()
        }

        // onclick back
        backBtn.setOnClickListener(View.OnClickListener { view ->
            setResult(Activity.RESULT_CANCELED)
            finish()
        })

        // onclick checkout
        checkOutBtn.setOnClickListener(View.OnClickListener { view ->
            if (cash) {
                if (validateFormCash()) {
                    setResult(Activity.RESULT_OK)
                    handleCheckOut()
                }
            } else {
                if (validateFormCard()) {
                    setResult(Activity.RESULT_OK)
                    handleCheckOut()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        clearForm()
    }

    fun handleCheckOut() {
        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

        var bill_id = 1

        GlobalScope.launch(Dispatchers.IO) {
            Log.i(screenName, "Call get next bill id")

            try {
                val response = retrofit.getBillId()
                if (response.isSuccessful) {
                    Log.i(screenName, "Success call get next bill id")

                    val responseBody = response.body()!!

                    bill_id = responseBody.bill_id.toInt() + 1

                    // post all bill order item
                    fullCartItems.forEach {
                        sendBillDetail(bill_id, it.food_id, it.item_qty)
                    }

                    // create bill
                    sendBillStatus(bill_id, fullCartItems[0].user_id)

                    // delete current cart
                    deleteCurrentCart(fullCartItems[0].user_id)

                }
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()

                // post all bill order item
                fullCartItems.forEach {
                    sendBillDetail(bill_id, it.food_id, it.item_qty)
                }

                // create bill
                sendBillStatus(bill_id, fullCartItems[0].user_id)

                // delete current cart
                deleteCurrentCart(fullCartItems[0].user_id)
            }
        }

        finish()
    }

    fun sendBillDetail(bill_id: Int, food_id: Int, qty: Int) {
        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.i(screenName, "Call post bill order item")
                retrofit.createBillDetail(BillDetail(bill_id, food_id, qty)).await()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }

        }
    }

    fun sendBillStatus(bill_id: Int, user_id: Int) {
        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

        var c: Calendar = Calendar.getInstance()
        var df: SimpleDateFormat? = null
        df = SimpleDateFormat("dd-MM-yyyy HH:mm a")

        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.i(screenName, "Call post bill")

                retrofit.createBillStatus(
                    BillStatus(
                        bill_id,
                        user_id,
                        checkOutPhone.text.toString(),
                        checkOutAddress.text.toString(),
                        df!!.format(c.time),
                        getPaymentMethod(),
                        cartDiscount,
                        cartDelivery,
                        cartTotal,
                        isPaid(),
                        1
                    )
                ).await()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun deleteCurrentCart(user_id: Int) {
        val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.i(screenName, "Call delete current cart")

                retrofit.deleteCart(
                    user_id
                ).await()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }

        }
    }

    fun getPaymentMethod(): String {
        if (cash) {
            return "cash"
        } else {
            return "card"
        }
    }

    fun isPaid(): String {
        if (cash) {
            return "false"
        } else {
            return "true"
        }
    }


    fun clearForm() {
        cardNumber.text.clear()
        cardName.text.clear()
        cardExpiryDate.text.clear()
        cardCvv.text.clear()
    }

    fun validatePhone(): Boolean {
        if (checkOutPhone.text.isEmpty()) {
            showMessage(R.id.checkOutPhone, "Entering a phone number is required.")
            return false
        }

        if (!checkOutPhone.text.toString().startsWith("84")) {
            showMessage(R.id.checkOutPhone, "Phone numbers must start with 84.")
            return false
        }

        if (checkOutPhone.text.length != 11) {
            showMessage(R.id.checkOutPhone, "Phone numbers must have exactly 11 digits.")
            return false
        }

        if (!checkOutPhone.text.matches(Regex("[0-9]{11}"))) {
            showMessage(R.id.checkOutPhone, "Phone numbers can only contain numbers.")
            return false
        }

        return true
    }

    fun validateAddress(): Boolean {
        if (checkOutAddress.text.isEmpty()) {
            showMessage(R.id.checkOutAddress, "Entering an address is required.")
            return false
        }

        return true
    }

    fun validateCardNumber(): Boolean {
        if (cardNumber.text.isEmpty()) {
            showMessage(R.id.cardNumber, "Entering the card number is required.")
            return false
        }

        if (!cardNumber.text.toString().startsWith("4")) {
            showMessage(R.id.cardNumber, "Card numbers must start with 4.")
            return false
        }

        if (cardNumber.text.length != 16) {
            showMessage(R.id.cardNumber, "Card numbers must have exactly 16 digits.")
            return false
        }

        if (!cardNumber.text.matches(Regex("[0-9]{16}"))) {
            showMessage(R.id.cardNumber, "Card numbers can only contain numbers.")
            return false
        }
        return true
    }

    fun validateCardName(): Boolean {
        if (cardName.text.isEmpty()) {
            showMessage(R.id.cardName, "Entering the card name is required.")
            return false
        }

        if (!cardName.text.replace(Regex("\\s+"), "").matches(Regex("^[A-Za-z]+\$"))) {
            showMessage(R.id.cardName, "The card name can only contain letters.")
            return false
        }

        return true
    }

    fun validateCardExpiry(): Boolean {
        if (cardExpiryDate.text.isEmpty()) {
            showMessage(R.id.cardExpiry, "Entering the card expiry data is required.")
            return false
        }

        if (cardExpiryDate.text.length != 5) {
            showMessage(R.id.cardExpiry, "Invalid date.")
            return false
        }

        if (!cardExpiryDate.text.contains("-")) {
            showMessage(R.id.cardExpiry, "Invalid date.")
            return false
        }


        return true
    }

    fun validateCvv(): Boolean {
        if (cardCvv.text.isEmpty()) {
            showMessage(R.id.cardCvv, "Entering the card cvv is required.")
            return false
        }

        if (cardCvv.text.length != 3) {
            showMessage(R.id.cardCvv, "CVV numbers must have exactly 3 digits.")
            return false
        }

        return true
    }

    fun validateFormCash(): Boolean {
        if (!validatePhone()) {
            return false
        }

        if (!validateAddress()) {
            return false
        }

        return true
    }

    fun validateFormCard(): Boolean {
        if (!validatePhone()) {
            return false
        }

        if (!validateAddress()) {
            return false
        }

        if (!validateCardNumber()) {
            return false
        }

        if (!validateCardName()) {
            return false
        }

        if (!validateCardExpiry()) {
            return false
        }

        if (!validateCvv()) {
            return false
        }

        return true
    }


    fun showMessage(itemId: Int, message: String) {
        Snackbar.make(findViewById(itemId), message, Snackbar.LENGTH_SHORT).show()
    }

    fun getImage(src: String): Drawable? {
        val imageSrc: InputStream = assets.open(src)
        val d = Drawable.createFromStream(imageSrc, null)
        return d
    }
}