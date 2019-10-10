package com.bombadu.aprikot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var addButton = findViewById<Button>(R.id.addButton)
        var minusButton = findViewById<Button>(R.id.minusButton)
        var textView = findViewById<TextView>(R.id.textView)
        var saveToListButton = findViewById<Button>(R.id.saveButton)
        var listView = findViewById<ListView>(R.id.list_view)
        var num = 0
        val list = mutableListOf<Int>()

        var adapter = ArrayAdapter<Int>(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter


        addButton.setOnClickListener(){
            num += 1
            textView.setText("$num")
        }

        minusButton.setOnClickListener() {

            if (num < 1) {
                Toast.makeText(this, "cant go below zero", Toast.LENGTH_SHORT).show()
            } else {
                num -= 1
                textView.setText("$num")
            }

        }

        saveToListButton.setOnClickListener() {
            list.add(num)
            adapter.notifyDataSetChanged()

        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            var listText = listView.getItemAtPosition(position)
            Toast.makeText(this, "Text: $listText", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId == R.id.about) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Aprikot v1.0")
                builder.setMessage("by Michael May" + "\n" + "Bombadu 2019")
                builder.setIcon(R.mipmap.ic_launcher)
                builder.setPositiveButton("ok") {dialog, which ->
                    Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show()
                }
                builder.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
