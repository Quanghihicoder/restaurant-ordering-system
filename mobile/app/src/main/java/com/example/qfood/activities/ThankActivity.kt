package com.example.qfood.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.qfood.R

class ThankActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank)

        val continueBtn = findViewById<CardView>(R.id.continueThank)
        continueBtn.setOnClickListener(View.OnClickListener { view ->
            setResult(Activity.RESULT_OK)
            finish()
        })
    }
}