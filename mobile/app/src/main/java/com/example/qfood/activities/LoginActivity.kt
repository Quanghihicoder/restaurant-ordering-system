package com.example.qfood.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.qfood.R
import com.example.qfood.activities.admin.AdminActivity
import com.example.qfood.apis.ServiceBuilder
import com.example.qfood.apis.ServiceInterface
import com.example.qfood.item.User
import com.example.qfood.item.UserPost
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val screenName = "Login - Register Screen"

    var login = true

    var adminPressCount = 0
    var adminPassword = "25082002"

    private lateinit var backBtn: ImageButton
    private lateinit var formTitle: TextView
    private lateinit var formNameInput: EditText
    private lateinit var formEmailInput: EditText
    private lateinit var formPhoneInput: EditText
    private lateinit var formPasswordInput: EditText
    private lateinit var formRePasswordInput: EditText
    private lateinit var helperTitle: TextView
    private lateinit var helperBtn: TextView
    private lateinit var processBtn: Button

    lateinit var dataUser: User

    fun switchMode(mode: Boolean) {
        if (mode) {
            formTitle.text = "Login"
            formNameInput.visibility = View.GONE
            formPhoneInput.visibility = View.GONE
            formRePasswordInput.visibility = View.GONE
            helperTitle.text = "Don't Have An Account?"
            helperBtn.text = "Create One"
            processBtn.text = "I am hungry"
        } else {
            formTitle.text = "Register"
            formNameInput.visibility = View.VISIBLE
            formPhoneInput.visibility = View.VISIBLE
            formRePasswordInput.visibility = View.VISIBLE
            helperTitle.text = "Have An Account?"
            helperBtn.text = "Login"
            processBtn.text = "Let’s eat"
        }
        login = mode
        clearForm()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.i(screenName, "On Create")

        // set init
        backBtn = findViewById<ImageButton>(R.id.loginBackBtn)
        formTitle = findViewById<TextView>(R.id.loginFormTitle)
        formNameInput = findViewById<EditText>(R.id.loginNameInput)
        formEmailInput = findViewById<EditText>(R.id.loginEmailInput)
        formPhoneInput = findViewById<EditText>(R.id.loginPhoneInput)
        formPasswordInput = findViewById<EditText>(R.id.loginPasswordInput)
        formRePasswordInput = findViewById<EditText>(R.id.loginRePasswordInput)
        helperTitle = findViewById<TextView>(R.id.loginHelperTitle)
        helperBtn = findViewById<TextView>(R.id.loginHelperBtn)
        processBtn = findViewById<Button>(R.id.loginBtn)

        login = intent.getBooleanExtra("mode", true)

        switchMode(login)

        backBtn.setOnClickListener(View.OnClickListener { view ->
            finish()
        })

        helperBtn.setOnClickListener(View.OnClickListener { view ->
            switchMode(!login)
        })

        processBtn.setOnClickListener(View.OnClickListener { view ->
            if (login) {

                if (adminPressCount == 3 && formEmailInput.text.toString() == "admin" && formPasswordInput.text.toString() == adminPassword) {
                    handleAdminAccess()
                } else {
                    handleLogin()
                }

            } else {
                handleRegister()
            }
        })

    }

    override fun onResume() {
        super.onResume()
        clearForm()
    }

    fun handleLogin() {
        if (formEmailInput.text.toString() == "admin") {
            adminPressCount = adminPressCount + 1
        }

        if (validateFormLogin()) {
            val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

            Log.i(screenName, "Call get user")

            retrofit.getUserByEmail(formEmailInput.text.toString()).enqueue(object :
                Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    try {
                        Log.i(screenName, "Success call get user")

                        val responseBody = response.body()!!
                        dataUser = responseBody

                        // check if match password
                        if (dataUser.user_password == formPasswordInput.text.toString()) {
                            saveUserInfo(
                                dataUser.user_id,
                                dataUser.user_name,
                                dataUser.user_email,
                                dataUser.user_phone
                            )
                        } else {
                            showMessage(R.id.loginBtn, "Incorrect email or password!")
                        }

                    } catch (ex: java.lang.Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d(screenName, "Fail call get user" + "Email not exist")
                    showMessage(R.id.loginBtn, "Incorrect email or password!")
                    Log.e("Retrofit", "Error: ${t.message}")
                }

            })
        }
    }

    fun handleRegister() {
        if (validateFormRegister()) {
            val retrofit = ServiceBuilder.buildService(ServiceInterface::class.java)

            Log.i(screenName, "Call get user")

            retrofit.getUserByEmail(formEmailInput.text.toString()).enqueue(object :
                Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    try {
                        Log.i(screenName, "Success Call get user")

                        showMessage(R.id.loginBtn, "Account already exist!")
                    } catch (ex: java.lang.Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d(screenName, "Fail call get user" + "Email not exist")

                    Log.i(screenName, "Call create user")

                    retrofit.createUser(
                        UserPost(
                            formNameInput.text.toString(),
                            formEmailInput.text.toString(),
                            formPhoneInput.text.toString(),
                            formPasswordInput.text.toString(),
                            "undefined",
                            "undefined",
                        )
                    )
                        .enqueue(object : Callback<Any> {
                            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                                Log.i(screenName, "Success call create user")
                            }

                            override fun onFailure(call: Call<Any>, t: Throwable) {
                                Log.d(screenName, "Fail call create user" + t.message)
                            }
                        })

                    // change to login
                    switchMode(!login)
                }
            })
        }
    }

    // save login data
    fun saveUserInfo(userId: Int, userName: String, userEmail: String, userPhone: String) {
        val sharedPreferences = getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putInt("USER_ID", userId)
            putString("USER_NAME", userName)
            putString("USER_EMAIL", userEmail)
            putString("USER_PHONE", userPhone)
            putString("USER_TYPE", "USER")
        }.apply()

        startActivity(Intent(this, MainActivity::class.java))
    }

    // handle admin
    private fun handleAdminAccess() {
        val sharedPreferences = getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("USER_TYPE", "ADMIN")
        }.apply()

        startActivity(Intent(this, AdminActivity::class.java))
    }

    fun clearForm() {
        formNameInput.text.clear()
        formEmailInput.text.clear()
        formPhoneInput.text.clear()
        formPasswordInput.text.clear()
        formRePasswordInput.text.clear()

        if (login) {
            formEmailInput.requestFocus()
        } else {
            formNameInput.requestFocus()
        }
    }

    fun validateName(): Boolean {
        if (formNameInput.text.isEmpty()) {
            showMessage(R.id.loginNameInput, "Entering a name is required.")
            return false
        }

        if (!formNameInput.text.replace(Regex("\\s+"), "").matches(Regex("^[A-Za-z]+\$"))) {
            showMessage(R.id.loginNameInput, "A name can only contain letters.")
            return false
        }

        return true
    }

    fun validateEmail(): Boolean {
        if (formEmailInput.text.isEmpty()) {
            showMessage(R.id.loginEmailInput, "Entering an email is required.")
            return false
        }

        if (!formEmailInput.text.matches(Regex("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}\$"))) {
            showMessage(R.id.loginEmailInput, "Email must be valid.")
            return false
        }

        return true
    }

    fun validatePhone(): Boolean {
        if (formPhoneInput.text.isEmpty()) {
            showMessage(R.id.loginPhoneInput, "Entering a phone number is required.")
            return false
        }

        if (!formPhoneInput.text.toString().startsWith("84")) {
            showMessage(R.id.loginPhoneInput, "Phone numbers must start with 84.")
            return false
        }

        if (formPhoneInput.text.length != 11) {
            showMessage(R.id.loginPhoneInput, "Phone numbers must have exactly 11 digits.")
            return false
        }

        if (!formPhoneInput.text.matches(Regex("[0-9]{11}"))) {
            showMessage(R.id.loginPhoneInput, "Phone numbers can only contain numbers.")
            return false
        }

        return true
    }

    fun validatePassword(): Boolean {
        if (formPasswordInput.text.isEmpty()) {
            showMessage(R.id.loginPasswordInput, "Password is required.")
            return false
        }

        if (formPasswordInput.text.length < 8) {
            showMessage(
                R.id.loginPasswordInput,
                "Password must be more than or equal 8 characters."
            )
            return false
        }

        return true
    }

    fun validateRePassword(): Boolean {
        if (formRePasswordInput.text.isEmpty()) {
            showMessage(R.id.loginRePasswordInput, "Confirm password is required.")
            return false
        }

        if (!formPasswordInput.text.toString().equals(formRePasswordInput.text.toString())) {
            showMessage(R.id.loginPasswordInput, "Confirm password must be match with password.")
            return false
        }

        return true
    }

    fun showMessage(itemId: Int, message: String) {
        Snackbar.make(findViewById(itemId), message, Snackbar.LENGTH_SHORT).show()
    }

    fun validateFormLogin(): Boolean {
        if (!validateEmail()) {
            return false
        }

        if (!validatePassword()) {
            return false
        }

        return true
    }


    fun validateFormRegister(): Boolean {
        if (!validateName()) {
            return false
        }

        if (!validateEmail()) {
            return false
        }

        if (!validatePhone()) {
            return false
        }

        if (!validatePassword()) {
            return false
        }

        if (!validateRePassword()) {
            return false
        }

        return true
    }
}