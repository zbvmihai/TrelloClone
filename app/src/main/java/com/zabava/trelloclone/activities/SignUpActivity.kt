package com.zabava.trelloclone.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.zabava.trelloclone.R
import com.zabava.trelloclone.databinding.ActivitySignUpBinding
import com.zabava.trelloclone.firebase.FirestoreClass
import com.zabava.trelloclone.models.User

@Suppress("DEPRECATION")
class SignUpActivity : BaseActivity() {
    private var binding: ActivitySignUpBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupActionBar()
        binding?.btnSignUp?.setOnClickListener { registerUser() }
    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarSignUpActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        binding?.toolbarSignUpActivity?.setNavigationOnClickListener { onBackPressed() }
    }

    @SuppressLint("RestrictedApi")
    private fun registerUser(){
        val name: String = binding?.etNameSignup?.text.toString().trim{it <= ' '}
        val email: String = binding?.etEmailSignup?.text.toString().trim{it <= ' '}
        val password: String = binding?.etPasswordSignup?.text.toString().trim{it <= ' '}

        if (validateForm(name,email,password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    hideProgressDialog()
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    val registeredEmail = firebaseUser.email!!

                    val user = User(
                        firebaseUser.uid, name, registeredEmail
                    )
                    FirestoreClass().registerUser(this,user)
                    finish()
                } else {
                    hideProgressDialog()
                    Toast.makeText(
                        this, "Registration failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun validateForm(name:String,email:String,password:String): Boolean{
        return when{
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please enter a name!")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please enter an email!")
                false
            }
            TextUtils.isEmpty(password)->{
                showErrorSnackBar("Please enter a password!")
                false
            }
            else->{
                true
            }

        }
    }

    fun userRegisteredSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this, "You have successfully registered!",
            Toast.LENGTH_SHORT
        ).show()
        FirebaseAuth.getInstance().signOut()
        finish()
    }
}