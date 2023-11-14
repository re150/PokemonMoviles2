package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
     private  lateinit var recyclerbtn : Button
    private  lateinit var addpokemo : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerbtn = findViewById(R.id.recyclerviewbtn)
        addpokemo = findViewById(R.id.uploadBtn)
        recyclerbtn.setOnClickListener {
            var i = Intent(this,UserlistActivity::class.java)
            startActivity(i)
        }
        addpokemo.setOnClickListener {
            var i = Intent(this,AddPokemon::class.java)
            startActivity(i)
        }
    }
}