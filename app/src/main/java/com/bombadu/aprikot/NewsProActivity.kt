package com.bombadu.aprikot

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

class NewsProActivity : AppCompatActivity(), NewsAdapter.ItemClickCallback {
    private val client = OkHttpClient()
    private var source = ""
    private var titleList = mutableListOf<String>()
    private var descriptionList = mutableListOf<String>()
    private var authorList = mutableListOf<String>()
    private var imageUrlList = mutableListOf<String>()
    private var spinnerList = mutableListOf<String>()
    private var webUrlList = mutableListOf<String>()
    private var listData = mutableListOf<String>()
    private var recyclerView : RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_pro_activity)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        populateSpinnerList()
        source = "ars-technica"

    }

    private fun populateSpinnerList() {
        //spinnerArrayList = ArrayList()
        spinnerList.add("ars-technica")
        spinnerList.add("engadget")
        spinnerList.add("recode")
        spinnerList.add("the-next-web")
        spinnerList.add("the-verge")
        spinnerList.add("the-wall-street-journal")
    }

    private fun fetchData() {

        //titleArrayList = ArrayList()
        titleList.clear()
        descriptionList.clear()
        authorList.clear()
        imageUrlList.clear()
        webUrlList.clear()


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

                        titleList.add(title)
                        descriptionList.add(description)
                        imageUrlList.add(imageUrls)
                        authorList.add(author)
                        webUrlList.add(webUrl)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                //Similar to Post Execute
                if (response.isSuccessful) runOnUiThread {
                    listData = getListData() as MutableList<String>
                    recyclerView!!.layoutManager = LinearLayoutManager(this@NewsProActivity)
                    val adapter = NewsAdapter(getListData(), this@NewsProActivity)
                    recyclerView!!.adapter = adapter
                    adapter.setItemClickCallback(this@NewsProActivity)
                }
            }
        })


    }

    private fun getListData(): List<ListItem> {
        val data = ArrayList<ListItem>()
        var i = 0
        while (i < titleList.size && i < descriptionList.size && i < imageUrlList.size && i < authorList.size && i < webUrlList.size) {
            val item = ListItem()
            item.authorText = authorList.get(i)
            item.titleText = titleList[i]
            item.descriptionText = descriptionList[i]
            item.imageUrlText = imageUrlList[i]
            item.webUrlText = webUrlList[i]
            data.add(item)
            i++
        }

        return data

    }

    override fun onItemClick(p: Int) {
        val item = listData[p] as ListItem
        val url = item.webUrlText
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val spinnerArrayAdapter =
            ArrayAdapter(this, R.layout.list_item_spinner, R.id.list_item_item, spinnerList)
        menuInflater.inflate(R.menu.news_pro_menu, menu)
        val item = menu.findItem(R.id.npSpinner)
        val spinner = item.getActionView() as Spinner
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
