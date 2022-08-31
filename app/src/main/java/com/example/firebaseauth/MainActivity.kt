package com.example.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnEmailPassword = findViewById<TextView>(R.id.btnEmailPassword)
        btnEmailPassword.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }
        val btnFacebook = findViewById<TextView>(R.id.btnFacebook)
        btnFacebook.setOnClickListener{

        }
        val btnPhoneNumber = findViewById<TextView>(R.id.btnPhoneNumber)
        btnPhoneNumber.setOnClickListener{

        }
    }
}