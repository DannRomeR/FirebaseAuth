package com.example.firebaseauth

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login2.*


class LoginActivity : AppCompatActivity() {

    private lateinit var progressBar2: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        /*txtUserEmail = findViewById(R.id.txtUserEmail)
        txtPasswordLogin2 =findViewById(R.id.txtPasswordLogin2)
        progressBar2 = findViewById(R.id.progressBar2)

        auth = FirebaseAuth.getInstance()
*/
        // Setup
        setup()

    }

    private fun setup() {

        title = "Authentication"
        btnLogin.setOnClickListener {
            if(txtUserEmail.text.isNotEmpty() &&
                txtPasswordLogin2.text.isNotEmpty()){

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(txtUserEmail.text.toString(),
                        txtPasswordLogin2.text.toString()).addOnCompleteListener(){
                            if(it.isSuccessful){
                                showHome(it.result?.user?.email ?: "", ProviderType.EMAILPASS)
                            }else{
                                showAlert()
                            }
                    }
            }
        }

        forgot.setOnClickListener{
           // progressBar2.visibility = android.view.View.VISIBLE
            startActivity(Intent(this,ForgotPassActivity::class.java))
        }

        txtViewAccount.setOnClickListener{
           //progressBar2.visibility = android.view.View.VISIBLE
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("There is an error with the Authentication of the user")
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType){
        val homeIntent = Intent(this, MenuActivity::class.java).apply {
            putExtra("email",email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

   /* private fun loginUser(){
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
}*/

}