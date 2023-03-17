package com.zabava.trelloclone.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.zabava.trelloclone.R
import com.zabava.trelloclone.databinding.ActivitySignInBinding
import com.zabava.trelloclone.firebase.FirestoreClass
import com.zabava.trelloclone.models.User

@Suppress("DEPRECATION")
class SignInActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private var binding: ActivitySignInBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = FirebaseAuth.getInstance()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupActionBar()

        binding?.btnSignIn?.setOnClickListener { signInRegisteredUser() }
    }

    fun signInSuccess(user: User){
        hideProgressDialog()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarSignInActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        binding?.toolbarSignInActivity?.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {

            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter an email!")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password!")
                false
            }
            else -> {
                true
            }
        }
    }

    private fun signInRegisteredUser() {
        val email: String = binding?.etEmail?.text.toString().trim { it <= ' ' }
        val password: String = binding?.etPassword?.text.toString().trim { it <= ' ' }

        if (validateForm(email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialog()
                    if (task.isSuccessful){
                        FirestoreClass().signInUser(this)
                    } else {
                        Log.w("Sign in", "signInWithEmail:failure")
                        Toast.makeText(this,"Authentication failed",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}