package com.example.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    /*private lateinit var txtName:EditText
    private lateinit var txtLastName:EditText
    private lateinit var txtEmail:EditText
    private lateinit var txtPassword:EditText
    private lateinit var progressBar:ProgressBar
    private lateinit var database:FirebaseDatabase
    private lateinit var auth:FirebaseAuth*/
    private lateinit var dbReference:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
       /* txtName = findViewById(R.id.txtName)
        txtLastName =findViewById(R.id.txtLastName)
        txtEmail =findViewById(R.id.txtEmail)
        txtPassword =findViewById(R.id.txtPassword)

        progressBar = findViewById(R.id.progressBar)


        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReference = database.reference.child("User")

        val btnRegister = findViewById<Button>(R.id.register)
        btnRegister.setOnClickListener{
            createNewAccount()
        }*/

        setup()
    }

    private fun setup(){

        title = "Authentication"
        register.setOnClickListener {
            if(txtName.text.isNotEmpty() &&
                txtLastName.text.isNotEmpty() &&
                txtEmail.text.isNotEmpty() &&
                txtPassword.text.isNotEmpty()){

                val name:String = txtName.text.toString()
                val lastName:String = txtLastName.text.toString()

                progressBar.visibility = android.view.View.VISIBLE
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtEmail.text.toString(),
                    txtPassword.text.toString()).addOnCompleteListener(){

                    if (it.isSuccessful){

                        //Add to database
                        /*val user:FirebaseUser? = FirebaseAuth.getInstance().currentUser
                        verifyEmail(user)
                        val userBD = dbReference.child(user?.uid.toString())
                        userBD.child("Name").setValue(name)
                        userBD.child("lastName").setValue(lastName)*/

                        //Send data
                        startActivity(Intent(this,LoginActivity::class.java))
                    }else{
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("There is an error with the creation of the user")
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


     /*fun register(view: View){
        createNewAccount()
    }*/
  /*  private fun createNewAccount(){
        val name:String = txtName.text.toString()
        val lastName:String = txtLastName.text.toString()
        val email:String = txtEmail.text.toString()
        val password:String = txtPassword.text.toString()

        if(!TextUtils.isEmpty(name) &&
            !TextUtils.isEmpty(lastName) &&
            !TextUtils.isEmpty(email) &&
            !TextUtils.isEmpty(password)){
            progressBar.visibility = android.view.View.VISIBLE

            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){
                    task->
                    if(task.isComplete){
                        val user:FirebaseUser? = auth.currentUser
                        verifyEmail(user)
                        val userBD = dbReference.child(user?.uid.toString())
                        userBD.child("Name").setValue(name)
                        userBD.child("lastName").setValue(lastName)
                        action()
                    }
                }
        }
    }

    private fun action(){
        startActivity(Intent(this,LoginActivity::class.java))
    }*/

    private fun verifyEmail(user:FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                task->

                if(task.isComplete){
                    Toast.makeText(this,"Email has send", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this,"An error has occurred while sending email", Toast.LENGTH_LONG).show()
                }
            }
    }
}