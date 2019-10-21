package com.bombadu.aprikot

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.util.ArrayList

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class NewsProActivity : AppCompatActivity(), NewsAdapter.ItemClickCallback {
    private var recyclerView: RecyclerView? = null
    private val client = OkHttpClient()
    private val jsonObject: JsonObject? = null
    private var source = ""
    private var titleArrayList: ArrayList<String>? = null
    private var descriptionArrayList: ArrayList<String>? = null
    private var authorArrayList: ArrayList<String>? = null
    private var imageUrlArrayList: ArrayList<String>? = null
    private var spinnerArrayList: ArrayList<String>? = null
    private var webUrlArrayList: ArrayList<String>? = null
    private var listData: ArrayList<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_pro_activity)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        recyclerView = findViewById(R.id.recyclerview)

        populateSpinnerList()


        source = "ars-technica"

        //fetchData();


    }

    private fun populateSpinnerList() {
        spinnerArrayList = ArrayList()
        spinnerArrayList!!.add("ars-technica")
        spinnerArrayList!!.add("engadget")
        spinnerArrayList!!.add("recode")
        spinnerArrayList!!.add("the-next-web")
        spinnerArrayList!!.add("the-verge")
        spinnerArrayList!!.add("the-wall-street-journal")
    }

    private fun fetchData() {

        titleArrayList = ArrayList()
        descriptionArrayList = ArrayList()
        authorArrayList = ArrayList()
        imageUrlArrayList = ArrayList()
        webUrlArrayList = ArrayList()


        val url =
            "https://newsapi.org/v1/articles?source=$source&apiKey=6b9392d84b4b4f2983e2b3c9d8f2c090"
        val request = Request.Builder().url(url).build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //Do Nothing
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val myResponse = response.body!!.string()
                Log.d("REPONSE", myResponse)
                try {
                    val jsonObject = JSONObject(myResponse)
                    val articlesJA = jsonObject.getJSONArray("articles")
                    for (i in 0 until articlesJA.length()) {
                        val jsonIndex = articlesJA.getJSONObject(i)
                        val title = jsonIndex.getString("title")
                        val description = jsonIndex.getString("description")
                        val imageUrls = jsonIndex.getString("urlToImage")
                        val webUrl = jsonIndex.getString("url")
                        var author = jsonIndex.getString("author")
                        if (author == "null") {
                            author = "by Anonymous"
                        } else {
                            author = "by $author"
                        }

                        titleArrayList!!.add(title)
                        descriptionArrayList!!.add(description)
                        imageUrlArrayList!!.add(imageUrls)
                        authorArrayList!!.add(author)
                        webUrlArrayList!!.add(webUrl)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                //Similar to Post Execute
                if (response.isSuccessful) {
                    runOnUiThread {
                        listData = getListData() as ArrayList<*>
                        recyclerView!!.layoutManager = LinearLayoutManager(this@NewsProActivity)
                        val adapter = NewsAdapter(getListData(), this@NewsProActivity)
                        recyclerView!!.adapter = adapter
                        adapter.setItemClickCallback(this@NewsProActivity)
                    }
                }
            }
        })


    }

    private fun getListData(): List<ListItem> {
        val data = ArrayList<ListItem>()
        var i = 0
        while (i < titleArrayList!!.size && i < descriptionArrayList!!.size && i < imageUrlArrayList!!.size && i < authorArrayList!!.size && i < webUrlArrayList!!.size) {
            val item = ListItem()
            item.authorText = authorArrayList!![i]
            item.titleText = titleArrayList!![i]
            item.descriptionText = descriptionArrayList!![i]
            item.imageUrlText = imageUrlArrayList!![i]
            item.webUrlText = webUrlArrayList!![i]
            data.add(item)
            i++
        }

        return data

    }

    override fun onItemClick(p: Int) {
        val item = listData!![p] as ListItem
        val url = item.webUrlText
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val spinnerArrayAdapter =
            ArrayAdapter(this, R.layout.list_item_spinner, R.id.list_item_item, spinnerArrayList!!)
        menuInflater.inflate(R.menu.news_pro_menu, menu)
        val item = menu.findItem(R.id.npSpinner)
        val spinner = MenuItemCompat.getActionView(item) as Spinner
        spinner.adapter = spinnerArrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                source = spinner.getItemAtPosition(position) as String
                fetchData()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        return super.onCreateOptionsMenu(menu)
    }
}
