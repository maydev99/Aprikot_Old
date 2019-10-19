package com.bombadu.aprikot

import android.os.Bundle
import android.view.Menu
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import java.util.*

class ActionBarSpinner : AppCompatActivity() {
    //internal var myAdapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, myArrayList)
        menuInflater.inflate(R.menu.news_menu, menu)
        val item = menu.findItem(R.id.spinner)
        val spinner = MenuItemCompat.getActionView(item) as Spinner
        spinner
        spinner.adapter = myAdapter
        spinner.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id -> }
        return super.onCreateOptionsMenu(menu)
    }
}
