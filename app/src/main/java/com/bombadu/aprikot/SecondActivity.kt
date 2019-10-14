package com.bombadu.aprikot

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.second_acitvity.*

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_acitvity)

        val myItem = intent.getStringExtra("number")
        val bigTextView = findViewById<TextView>(R.id.textView2)
        val wordsTextView = findViewById<TextView>(R.id.arrayWordsTextView)

        bigTextView.text = myItem

        var wordList = mutableListOf<String>()
        wordList.add("Mike")
        wordList.add("Cate")
        wordList.add("Bob")
        wordList.add("Mary")
        wordList.add("Steve")
        wordList.add("Gwen")
        wordList.add("Hello")
        wordList.add("Wow!")
        wordList.add("Kotlin")

        wordList.sort()


        for (word in wordList) println(word)

        /*for (word in wordList) {
            println(word)

        }*/

        println(wordList.joinToString())






    }
}