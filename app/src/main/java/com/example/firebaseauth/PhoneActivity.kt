package com.example.firebaseauth

import android.app.ProgressDialog
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.firebaseauth.databinding.ActivityPhoneBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.core.view.View
import kotlinx.android.synthetic.main.activity_phone.*
import java.util.concurrent.TimeUnit

class PhoneActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityPhoneBinding

    //if code sending failed, will used to resend
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var mVerificationId: String? = ""
    private lateinit var firebaseAuth: FirebaseAuth

    private val TAG = "MAIN_TAG"

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)

//        phoneLl.visibility = android.view.View.VISIBLE
//        codeLl.visibility = android.view.View.GONE

        setup()
    }

    private fun setup() {
        title = "OTPMessage"

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Log.d(TAG,"onVerificationCompleted: ")
                signInWithPhoneAuthCredential(phoneAuthCredential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                progressDialog.dismiss()
                Log.d(TAG,"onVerificationFailed: ${e.message}")
                Toast.makeText(this@PhoneActivity,"${e.message}",Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken) {
               Log.d(TAG,"onCodeSent: $verificationId")
                mVerificationId = verificationId
                forceResendingToken = token
                progressDialog.dismiss()

               // Log.d(TAG,"onCodeSent: $token")
//                codeLl.visibility = android.view.View.VISIBLE
//                phoneLl.visibility = android.view.View.GONE

                Toast.makeText(this@PhoneActivity,"Verification code sent...", Toast.LENGTH_SHORT).show()
                //txtEnterOTP.text = "Please type the verification code we sent to ${txtPhoneNumber.text.toString().trim()}"
            }
        }
        btnSendOTP.setOnClickListener {
            if(txtPhoneNumber.text.isNotEmpty()){
                startPhoneNumberVerification(txtPhoneNumber.text.toString().trim())
            }
        }
        btnLoginOTP.setOnClickListener {
            verifyPhoneNumberWithCode(mVerificationId,et_otp.text.toString().trim())
                showHome(txtPhoneNumber.text.toString().trim() ?: "", ProviderType.PHONENUMBER)
        }
        resendCode.setOnClickListener {
            resendVerificationCode(txtPhoneNumber.text.toString().trim(), forceResendingToken)
        }
    }
    private fun showHome(phone: String, provider: ProviderType){
        val homeIntent = Intent(this, MenuActivity::class.java).apply {
            putExtra("email",phone)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
    private fun startPhoneNumberVerification(phoneNumber:String){
        Log.d(TAG,"startPhoneNumberVerification: $phoneNumber")
        progressDialog.setMessage("Verifying Phone Number...")
        progressDialog.show()
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallBacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    private fun resendVerificationCode (phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken?){
        progressDialog.setMessage("Resending Code...")
        progressDialog.show()

        Log.d(TAG,"resendVerificationCode: $phoneNumber")

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallBacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            options.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(options.build())
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code:String){
        Log.d(TAG,"verifyPhoneNumberWithCode: $verificationId $code")
        progressDialog.setMessage("Verifying Code...")
        progressDialog.show()

        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        Log.d(TAG,"signInWithPhoneAuthCredential: $credential")

        progressDialog.setMessage("Logging In...")

        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val phone = firebaseAuth.currentUser?.phoneNumber
                Toast.makeText(this,"Logged In as $phone",Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

}