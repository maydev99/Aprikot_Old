package com.bombadu.aprikot

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    var list = mutableListOf<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var addButton = findViewById<Button>(R.id.addButton)
        var minusButton = findViewById<Button>(R.id.minusButton)
        var textView = findViewById<TextView>(R.id.textView)
        var saveToListButton = findViewById<Button>(R.id.saveButton)
        var listView = findViewById<ListView>(R.id.list_view)
        //var hashSet = HashSet<String>()

        var num = 0

        loadPrefs()


        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
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
            list.add(num.toString())
            adapter.notifyDataSetChanged()
            saveToPrefs()

        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            var listText = listView.getItemAtPosition(position)
            var myItem = listText.toString()
            Toast.makeText(this, "Text: $listText", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("number", myItem)
            startActivity(intent)


        }

       listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
           list.removeAt(position)
           saveToPrefs()
           adapter.notifyDataSetChanged()
           true
       }
    }

    private fun loadPrefs() {
        var set :HashSet<String>
        try {
            val sharedPrefs: SharedPreferences = getSharedPreferences("prefs_key", Context.MODE_PRIVATE)
            set = sharedPrefs.getStringSet("prefs_key", null) as HashSet<String>
            list = set.toMutableList()
            list.sortBy { it.toBigInteger() }
        } catch (e: Exception){
            e.printStackTrace()

        }


    }

    private fun saveToPrefs() {
        list.sortBy { it.toBigInteger() }
        val set: HashSet<String> = HashSet(list)

        val sharedPrefs: SharedPreferences = getSharedPreferences("prefs_key", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.putStringSet("prefs_key", set)
        editor.commit()
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
