package com.example.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : AppCompatActivity() {
    private lateinit var txtEmailForgot: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar3: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        txtEmailForgot =findViewById(R.id.txtEmailForgot)
        progressBar3 = findViewById(R.id.progressBar3)
        auth = FirebaseAuth.getInstance()

        val btnForgotPass = findViewById<Button>(R.id.btnForgotPass)
        btnForgotPass.setOnClickListener{
            val email = txtEmailForgot.text.toString()

            if(!TextUtils.isEmpty(email)) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this){
                        task ->
                        if(task.isSuccessful){
                            progressBar3.visibility = View.VISIBLE
                            startActivity(Intent(this,LoginActivity::class.java))
                        }else{
                            Toast.makeText(this,"Error to send email", Toast.LENGTH_LONG).show()
                        }
                    }

            }
        }

    }
}