package com.bombadu.aprikot


import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class ImageFullActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_full)
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val imageUrl = intent.getStringExtra("image_url")
        val myImageView = findViewById<ImageView>(R.id.imageFullImageView)

        Picasso.get().load(imageUrl).into(myImageView)




    }
}