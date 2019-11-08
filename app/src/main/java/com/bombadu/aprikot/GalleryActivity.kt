package com.bombadu.aprikot

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.gallery_activity.*

class GalleryActivity : AppCompatActivity()  {


    var uploaderList = mutableListOf<String>()
    var timeList = mutableListOf<String>()
    var captionList = mutableListOf<String>()
    var imageUrlList = mutableListOf<String>()
    var keyList = mutableListOf<String>()
    private var listData = mutableListOf<String>()
    var rootRef = FirebaseDatabase.getInstance().reference
    var auth: FirebaseAuth? = null
    var authStateListener = FirebaseAuth.AuthStateListener {  }
    var email = "may@droidloft.com"
    var password = "joker11"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery_activity)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val galleryRecyclerView = findViewById<RecyclerView>(R.id.galleryRecyclerView)

        auth = FirebaseAuth.getInstance()
        auth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth!!.currentUser.toString()
                    //Toast.makeText(this, "$user logged in", Toast.LENGTH_SHORT).show()
                } else {
                    //Toast.makeText(this, "Auth Failed", Toast.LENGTH_SHORT).show()
                }
            }

        var galleryRef = rootRef.child("gallery")

        val galleryListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val children = dataSnapshot!!.children
                children.forEach {
                    var key = it.key.toString()

                    keyList.add(key)
                }

                for (i in 0 until keyList.size){
                    var myKey = keyList[i]
                    var time = dataSnapshot.child(myKey).child("time").value.toString()
                    var uploader = dataSnapshot.child(myKey).child("uploader").value.toString()
                    var caption = dataSnapshot.child(myKey).child("caption").value.toString()
                    var imageUrl = dataSnapshot.child(myKey).child("imageUrl").value.toString()

                    uploader = "Uploaded by: $uploader"

                    uploaderList.add(uploader)
                    captionList.add(caption)
                    imageUrlList.add(imageUrl)
                    timeList.add(time)
                }

                populateViews()

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }

        galleryRef.addValueEventListener(galleryListener)






    }

    private fun populateViews() {
        listData = getListData() as MutableList<String>
        galleryRecyclerView!!.layoutManager = LinearLayoutManager(this@GalleryActivity)
        val adapter = GalleryAdapter(getListData(), this@GalleryActivity)
        galleryRecyclerView!!.adapter = adapter
        //adapter.setItemClickCallback(this@GalleryActivity)
    }

    private fun getListData(): List<ListItem> {
        val data = ArrayList<ListItem>()
        var i =0
        while (i < uploaderList.size && i < captionList.size && i < imageUrlList.size && i < timeList.size){
            val item = ListItem()
            item.galleryCaptionText = captionList[i]
            item.galleryTimeDateText = timeList[i]
            item.galleryUploaderText = uploaderList[i]
            item.galleryUrlText = imageUrlList[i]
            data.add(item)
            i++
        }


        return data
    }

  /*  override fun onItemClick(p: Int) {
        val  item = listData[p] as ListItem
        val url = item.imageUrlText
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)

    }*/
}