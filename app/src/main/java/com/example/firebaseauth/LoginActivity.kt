package com.example.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var txtUserEmail: EditText
    private lateinit var txtPasswordLogin2: EditText
    private lateinit var progressBar2: ProgressBar
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        txtUserEmail = findViewById(R.id.txtUserEmail)
        txtPasswordLogin2 =findViewById(R.id.txtPasswordLogin2)
        progressBar2 = findViewById(R.id.progressBar2)

        auth = FirebaseAuth.getInstance()

        val btnForgot = findViewById<TextView>(R.id.forgot)
        btnForgot.setOnClickListener{

        }
        val btnLogin = findViewById<TextView>(R.id.btnLogin)
        btnLogin.setOnClickListener{
            loginUser()
        }
        val txtViewAccount = findViewById<TextView>(R.id.txtViewAccount)
        txtViewAccount.setOnClickListener{
            progressBar2.visibility = android.view.View.VISIBLE
            startActivity(Intent(this,RegisterActivity::class.java))
        }

    }

    private fun loginUser(){
        val user:String = txtUserEmail.text.toString()
        val password:String = txtPasswordLogin2.text.toString()

        if(!TextUtils.isEmpty(user) &&
            !TextUtils.isEmpty(password)){
            progressBar2.visibility = android.view.View.VISIBLE

            auth.signInWithEmailAndPassword(user,password)
                .addOnCompleteListener(this){
                    task->
                    if(task.isSuccessful){
                        action()
                    }else{
                        Toast.makeText(this,"The email and password are incorrect", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
private fun action(){
    startActivity(Intent(this,MenuActivity::class.java))
}

}