package com.bombadu.aprikot

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.util.ArrayList
import java.util.PriorityQueue

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class NewsActivity : AppCompatActivity() {

    private var newsListView: ListView? = null
    private val client = OkHttpClient()
    private var jsonObject: JSONObject? = null
    private var titleArrayList: ArrayList<String>? = null
    private var myAdapter: Adapter? = null
    var source = "ars-technica"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_activity)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        newsListView = findViewById(R.id.newsListView)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        var  myAdapter: ArrayAdapter<String>

        val myArrayList = ArrayList<String>()
        myArrayList.add("ars-technica")
        myArrayList.add("engadget")
        myArrayList.add("mashable")
        myArrayList.add("recode")
        myArrayList.add("techcrunch")
        myArrayList.add("tech_radar")
        myArrayList.add("the-verge")

        myAdapter = ArrayAdapter(this, R.layout.list_item_spinner, R.id.list_item_item, myArrayList)
        menuInflater.inflate(R.menu.news_menu, menu)
        val item = menu.findItem(R.id.spinner)
        val spinner = MenuItemCompat.getActionView(item) as Spinner
        spinner.adapter = myAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //do nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                source = spinner.getItemAtPosition(position) as String
                fetchData()



            }
        }
        return super.onCreateOptionsMenu(menu)
    }



    private fun fetchData() {
        val url = "https://newsapi.org/v1/articles?source="+ source + "&apiKey=6b9392d84b4b4f2983e2b3c9d8f2c090"
        val request = Request.Builder().url(url).build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("HTTP CALL", "Call Failed")

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                try {
                    titleArrayList = ArrayList()
                    jsonObject = JSONObject(response.body!!.string())
                    val articlesJA = jsonObject!!.getJSONArray("articles")
                    for (i in 0 until articlesJA.length()) {
                        val jsonIndex = articlesJA.getJSONObject(i)
                        val titles = jsonIndex.getString("title")
                        titleArrayList!!.add(titles)
                    }



                    Log.d("TITLES", titleArrayList!!.toString())


                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                if (response.isSuccessful) {



                    runOnUiThread { newsListView!!.adapter = myAdapter as ListAdapter?
                        myAdapter = ArrayAdapter(this@NewsActivity, android.R.layout.simple_list_item_1, titleArrayList!!)
                        newsListView!!.adapter = myAdapter as ListAdapter?
                    }
                }

            }


        })


    }
}
