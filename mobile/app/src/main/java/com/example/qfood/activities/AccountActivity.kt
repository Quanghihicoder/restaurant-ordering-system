package com.example.qfood.activities

import android.app.Activity
import android.content.Context
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
import java.io.InputStream

class AccountActivity : AppCompatActivity() {
    private val screenName = "Account Screen"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        Log.i(screenName, "On Create")

        // init
        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        val userNameText = findViewById<TextView>(R.id.userName)
        val userEmailText = findViewById<TextView>(R.id.userEmail)
        val userPhoneText = findViewById<TextView>(R.id.userPhone)
        val logoutBtn = findViewById<CardView>(R.id.logoutBtn)
        val background = findViewById<ImageView>(R.id.backgroundImage)
        background.setImageDrawable(getImage("account/background.png"))

        // get user data
        val sharedPreferences = getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        userNameText.text = sharedPreferences.getString("USER_NAME", "User name")
        userEmailText.text = sharedPreferences.getString("USER_EMAIL", "User email")
        userPhoneText.text = sharedPreferences.getString("USER_PHONE", "User phone")

        // onclick back
        backBtn.setOnClickListener(View.OnClickListener { view ->
            setResult(Activity.RESULT_CANCELED)
            finish()
        })

        // onclick logout
        logoutBtn.setOnClickListener(View.OnClickListener { view ->
            logout()
            setResult(Activity.RESULT_OK)
            finish()
        })
    }

    fun getImage(src: String): Drawable? {
        val imageSrc: InputStream = assets.open(src)
        val drawable = Drawable.createFromStream(imageSrc, null)
        return drawable
    }

    fun logout() {
        // delete user in share preferences
        val sharedPreferences = getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            remove("USER_ID")
            remove("USER_NAME")
            remove("USER_EMAIL")
            remove("USER_PHONE")
            remove("USER_TYPE")
        }.apply()
    }
}