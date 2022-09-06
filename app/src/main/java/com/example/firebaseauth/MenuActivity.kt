package com.example.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu.*

enum class ProviderType{
    EMAILPASS
}

class MenuActivity : AppCompatActivity() {

    private lateinit var progressBar4: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        //Setup

        val bundle :Bundle? = intent.extras
        val email :String? = bundle?.getString("email")
        val provider :String? = bundle?.getString("provider")
        setup(email?:"",provider?:"")


        btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()

        }
    }

    private fun setup(email: String, provider: String){
        title = "Start"
        emailTextView.text = email
        providerTextView.text = provider
    }
}