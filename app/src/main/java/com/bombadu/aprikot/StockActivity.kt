package com.bombadu.aprikot

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException

class StockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stock_activity)

        val newsListView = findViewById<ListView>(R.id.newsListView)


        fetchJson()

    }

    private fun fetchJson() {
        println("attempting to fetch json")

        val url = "https://newsapi.org/v1/articles?source=ars-technica&apiKey=6b9392d84b4b4f2983e2b3c9d8f2c090"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()
                println(body)

                val jsonObject = JsonObject().get(body)

                var titleList = mutableListOf<String>()
                var descriptionList = mutableListOf<String>()
                //var articlesJa = JsonArray().asJsonObject.getAsJsonArray("articles")
                var articlesJa = JsonArray().asJsonObject.getAsJsonArray("articles")
                println(articlesJa)

                /*for (i in 0 until articlesJa.size()){
                    //for (word in wordList) println(word)
                    //for (word in wordList.indices) println(wordList[word])incremental getting words in list

                    var jsonIndex = jsonObject.asJsonArray[i]
                    var titles = jsonIndex.("title").asString
                    titleList.add(titles)
                    println(titleList)

                   */



                //val gson = GsonBuilder().create()
                //val feed = gson.fromJson(body,HomeFeed::class.java)
            }
            override fun onFailure(call: Call, e: IOException) {
                println("failed to execute")
            }
        })


    }
}


class HomeFeed(val articles: List<String>)

class Article(val title: String, val description: String)