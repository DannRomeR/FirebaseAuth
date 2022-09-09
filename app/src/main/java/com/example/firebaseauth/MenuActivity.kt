package com.example.firebaseauth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login2.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_phone.*

enum class ProviderType{
    EMAILPASS,
    GOOGLE,
    PHONENUMBER
}

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        //Setup
        LinearNames.visibility = android.view.View.GONE
        val bundle :Bundle? = intent.extras
        val email :String? = bundle?.getString("email")
        val provider :String? = bundle?.getString("provider")

        if(provider == ProviderType.EMAILPASS.name){
            LinearNames.visibility = android.view.View.VISIBLE
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                val name = user.displayName
                txtNameFromDB.text = name.toString()
            }

            checkIfEmailVerified(user)
        }

        setup(email?:"",provider?:"")

        //Save data
       /* val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider",provider)
        prefs.apply()*/

    }

    private fun setup(email: String, provider: String){
        title = "Start"
        emailTextView.text = email
        providerTextView.text = provider

        btnLogout.setOnClickListener{
            //Delete data
           /* val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()*/

            if(provider == ProviderType.PHONENUMBER.name){
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this,LoginActivity::class.java))
                Toast.makeText(this,"Close User Session", Toast.LENGTH_LONG).show()
            }else{
                FirebaseAuth.getInstance().signOut()
                onBackPressed()
                Toast.makeText(this,"Close User Session", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun checkIfEmailVerified(user : FirebaseUser?){
        if (user != null) {
            if (user.isEmailVerified) {
                Toast.makeText(this,"Successfully logged in", Toast.LENGTH_LONG).show()
            }  else {
                FirebaseAuth.getInstance().signOut() //restart this activity
                startActivity(Intent(this,LoginActivity::class.java))
                Toast.makeText(this,"The user is not verified", Toast.LENGTH_LONG).show()

            }
        }
    }
}