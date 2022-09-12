package com.example.firebaseauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_menu.*


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
            val reference = FirebaseDatabase.getInstance().reference.child("User")

            /*reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for(snapshot in dataSnapshot.children){
                        val username =
                            snapshot.key
                        txtNameFromDB.text = username.toString()
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    txtNameFromDB.text = "Error"
                }
            })*/
            val rootRef = FirebaseDatabase.getInstance().reference
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val uidRef = rootRef.child("User").child(uid)
            uidRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val snapshot = task.result
                    val username = snapshot.child("Name").getValue(String::class.java)
                    txtNameFromDB.text = username
                } else {
                    Log.d("TAG", task.exception!!.message!!) //Don't ignore potential errors!
                }
            }

            /*user?.let {
                val name = user.displayName
                txtNameFromDB.text = name.toString()
            }*/

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