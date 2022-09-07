package com.example.firebaseauth

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login2.*


class LoginActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100
    private val callbackManager = CallbackManager.Factory.create()
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
        //session()
    }

    /*override fun onStart() {
        super.onStart()
        loginLayout.visibility = View.VISIBLE
    }

    private fun session(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email",null)
        val provider = prefs.getString("email",null)

        if(email != null && provider != null){
            loginLayout.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }
    }*/

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
           //progressBar2.visibility = View.VISIBLE
            startActivity(Intent(this,ForgotPassActivity::class.java))
        }

        txtViewAccount.setOnClickListener{
           //progressBar2.visibility = View.VISIBLE
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        btnGoogle.setOnClickListener {
            //Configuration
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient : GoogleSignInClient = GoogleSignIn.getClient(this,googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }
        btnFacebookLogin.setOnClickListener {

            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

            LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult>{
                override fun onSuccess(result: LoginResult?) {
                    result?.let {
                        val token = it.accessToken
                        val credential = FacebookAuthProvider.getCredential(token.token)
                        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                            if(it.isSuccessful){
                                showHome(it.result?.user?.email ?: "", ProviderType.FACEBOOK)
                            }else{
                                showAlert()
                            }
                        }
                    }
                }

                override fun onCancel() {

                }

                override fun onError(error: FacebookException?) {

                }
            })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode,resultCode,data)

        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if(account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{

                        if(it.isSuccessful){
                            showHome(account.email ?: "", ProviderType.GOOGLE)
                        }else{
                            showAlert()
                        }
                    }
                }
            }
            catch (e : ApiException){
                showAlert()
            }
        }
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