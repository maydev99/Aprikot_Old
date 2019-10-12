package com.bombadu.aprikot

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.second_acitvity.*

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_acitvity)

        val myItem = intent.getStringExtra("number")
        val bigTextView = findViewById<TextView>(R.id.textView2)

        bigTextView.text = myItem




    }
}