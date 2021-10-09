package com.example.thememers

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.VolleyError

import com.android.volley.Response

import com.android.volley.toolbox.StringRequest

import com.android.volley.toolbox.Volley

import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {
    lateinit var meme : ImageView
    lateinit var next : Button
    lateinit var share : Button
    var memeUrl : String? = null
    lateinit var progressBar : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        meme  = findViewById(R.id.meme)
        next = findViewById(R.id.loadnext)
        share = findViewById(R.id.share)
        share.setOnClickListener(){
            shareMeme(next)
        }
        progressBar = findViewById(R.id.progressBar)
        next.setOnClickListener(){
            nextMeme(next)
        }

        loadMeme()

    }
    private fun loadMeme(){
        progressBar.visibility = View.VISIBLE

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                memeUrl = response.getString("url")
                Glide.with(this).load(memeUrl).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                }).into(meme)
            },
            { error ->
                // TODO: Handle error
                Toast.makeText(this, "Not working", Toast.LENGTH_SHORT).show()
            })
                  queue.add(jsonObjectRequest)
    }

    fun nextMeme(view:View){
        loadMeme()
    }

    fun shareMeme(view:View){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey Checkout this cool meme sent through my app $memeUrl")
        val chooser = Intent.createChooser(intent,"Share this meme using...")
        startActivity(chooser)
    }

}