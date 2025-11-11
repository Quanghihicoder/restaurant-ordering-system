package com.example.qfood.activities

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.example.qfood.R
import com.example.qfood.activities.admin.AdminActivity
import java.io.IOException
import java.io.InputStream

class WelcomeActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var registerBtn: Button
    lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        performAccess()

        // set init
        imageView = findViewById<ImageView>(R.id.welcomeImage)
        registerBtn = findViewById<Button>(R.id.welcomeRegister)
        loginBtn = findViewById<Button>(R.id.welcomeLogin)

        // set image
        try {
            val imageSrc: InputStream = assets.open("welcome/food-stand.png")
            val d = Drawable.createFromStream(imageSrc, null)
            imageView.setImageDrawable(d)
        } catch (ex: IOException) {
            return
        }

        // on click
        val loginScreen = Intent(this, LoginActivity::class.java)
        registerBtn.setOnClickListener(View.OnClickListener { view ->
            loginScreen.putExtra("mode", false)
            startActivity(loginScreen)
        })
        loginBtn.setOnClickListener(View.OnClickListener { view ->
            loginScreen.putExtra("mode", true)
            startActivity(loginScreen)
        })

    }

    // check if already login
    fun performAccess() {
        val sharedPreferences = getSharedPreferences("sharePref", Context.MODE_PRIVATE)

        if (sharedPreferences.getString("USER_TYPE", "USER") == "ADMIN") {
            startActivity(Intent(this, AdminActivity::class.java))
        }
        else {
            if (sharedPreferences.getInt("USER_ID", -1) != -1) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}