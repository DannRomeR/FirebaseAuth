package com.example.firebaseauth

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebaseauth.databinding.ActivityPhoneBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.android.synthetic.main.activity_phone.*

class OtpActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityPhoneBinding

    //if code sending failed, will used to resend
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var mVerificationId: String? = null
    private lateinit var firebaseAuth: FirebaseAuth

    private val TAG = "MAIN_TAG"

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        setup()
    }

    private fun setup() {
        title = "PhoneAuthentication"
        btnLoginOTP.setOnClickListener {
            if(btnLoginOTP.text.isNotEmpty()){

            }
        }
    }
    }
